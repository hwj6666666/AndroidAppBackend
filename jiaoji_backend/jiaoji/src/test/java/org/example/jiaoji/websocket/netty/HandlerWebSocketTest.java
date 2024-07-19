package org.example.jiaoji.websocket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.example.jiaoji.websocket.ChatContextUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HandlerWebSocketTest {

    @Mock
    private ChatContextUtils chatContextUtils;

    @Mock
    private Logger logger;

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private Channel channel;

    @InjectMocks
    private HandlerWebSocket handlerWebSocket;

    @Mock
    private ChannelId channelId;

    @BeforeEach
    public void setUp() {
        lenient().when(ctx.channel()).thenReturn(channel);

        lenient().when(channel.id()).thenReturn(channelId);
        lenient().when(channelId.toString()).thenReturn("mockedChannelId");
//        when(channel.attr(any(AttributeKey.class))).thenReturn(attribute);
    }

    @Test
    public void testChannelRead0() throws Exception {
        TextWebSocketFrame frame = new TextWebSocketFrame("hello");
        AttributeKey<String> key = AttributeKey.valueOf(channel.id().toString());
        when(channel.attr(key)).thenReturn(new MockAttribute<>("user123"));

        handlerWebSocket.channelRead0(ctx, frame);

        verify(chatContextUtils).receiveMessage("user123", "hello");
    }

    @Test
    public void testChannelRead0Heartbeat() throws Exception {
        TextWebSocketFrame frame = new TextWebSocketFrame("heartbeat");

        handlerWebSocket.channelRead0(ctx, frame);

        verify(chatContextUtils, never()).receiveMessage(anyString(), anyString());
        verify(logger, never()).info(anyString(), anyString(), anyString());
    }

    @Test
    public void testChannelActive() throws Exception {
        handlerWebSocket.channelActive(ctx);
    }

    @Test
    public void testChannelInactive() throws Exception {
        handlerWebSocket.channelInactive(ctx);

        verify(chatContextUtils).removeContext(channel);
    }

    @Test
    public void testUserEventTriggered() throws Exception {
        WebSocketServerProtocolHandler.HandshakeComplete complete =
                mock(WebSocketServerProtocolHandler.HandshakeComplete.class);
        when(complete.requestUri()).thenReturn("/ws?userId=user123&chatId=chat456");

        handlerWebSocket.userEventTriggered(ctx, complete);

        verify(chatContextUtils).addContext("user123", channel, "chat456");
    }

    @Test
    public void testUserEventTriggeredInvalidUrl() throws Exception {
        WebSocketServerProtocolHandler.HandshakeComplete complete =
                mock(WebSocketServerProtocolHandler.HandshakeComplete.class);
        when(complete.requestUri()).thenReturn("/ws?invalid=params");

        handlerWebSocket.userEventTriggered(ctx, complete);

        verify(chatContextUtils, never()).addContext(anyString(), any(Channel.class), anyString());
    }

    @Test
    public void testUserEventTriggeredNonHandshakeCompleteEvent() throws Exception {
        Object nonHandshakeCompleteEvent = new Object();

        handlerWebSocket.userEventTriggered(ctx, nonHandshakeCompleteEvent);

        // Verify that nothing happens when a non-handshake complete event is passed
        verifyNoInteractions(chatContextUtils);
        verifyNoInteractions(logger);
    }

    private static class MockAttribute<T> implements io.netty.util.Attribute<T> {
        private T value;

        public MockAttribute(T value) {
            this.value = value;
        }

        @Override
        public AttributeKey<T> key() {
            return null;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public void set(T value) {
            this.value = value;
        }

        @Override
        public T getAndSet(T value) {
            T oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public T setIfAbsent(T value) {
            if (this.value == null) {
                this.value = value;
            }
            return this.value;
        }

        @Override
        public T getAndRemove() {
            T oldValue = this.value;
            this.value = null;
            return oldValue;
        }

        @Override
        public boolean compareAndSet(T oldValue, T newValue) {
            if (this.value.equals(oldValue)) {
                this.value = newValue;
                return true;
            }
            return false;
        }

        @Override
        public void remove() {
            this.value = null;
        }
    }
}
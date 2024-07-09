package org.example.jiaoji.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.example.jiaoji.pojo.Chat;
import org.example.jiaoji.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatContextUtils {
    private static ChatService chatService;

    private static final ConcurrentHashMap<String, Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public void addContext(String userId, Channel channel, String chatId){
        String channelId = channel.id().toString();
        @SuppressWarnings("rawtypes")
        AttributeKey attributeKey = null;
        if(!AttributeKey.exists(channelId)){
            attributeKey = AttributeKey.newInstance(channelId);
        }else{
            attributeKey = AttributeKey.valueOf(channelId);
        }
        channel.attr(attributeKey).set(userId);
        USER_CONTEXT_MAP.put(userId, channel);
        List<Chat> data = chatService.getAllChatWith(Integer.parseInt(chatId));
        sendMessage(userId, data);
    }

    public void receiveMessage(String userId, String message){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Chat chat = objectMapper.readValue(message, Chat.class);
            chat.setTime(LocalDateTime.now());
            chatService.addChat(chat);
            List<Chat> data = chatService.getAllChatWith((chat.getChatId()));
            sendMessage(userId, data);
            Integer receiveId = chatService.getTheOtherId(chat.getChatId(), chat.getSendId());
            sendMessage(receiveId.toString(), data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String UserId, List<Chat> data){
        Channel channel = USER_CONTEXT_MAP.get(UserId);
        if(channel == null){
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String message = mapper.writeValueAsString(data);
            channel.writeAndFlush(new TextWebSocketFrame(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // 这里可以加入日志记录或其他错误处理逻辑
        }
    }

    public void removeContext(Channel channel){
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        if(userId != null){
            USER_CONTEXT_MAP.remove(userId);
        }
    }

    @Autowired
    public void setChatService(ChatService chatService) {
        ChatContextUtils.chatService = chatService;
    }
}

package org.example.jiaoji.service.serverimpl;

import org.example.jiaoji.mapper.ChatItemMapper;
import org.example.jiaoji.mapper.ChatMapper;
import org.example.jiaoji.pojo.Chat;
import org.example.jiaoji.pojo.ChatItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private ChatMapper chatMapper;

    @Mock
    private ChatItemMapper chatItemMapper;

    @InjectMocks
    private ChatServiceImpl chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addChat() {
        Chat chat = new Chat();
        chat.setChatId(1);
        chat.setSendId(2);
        chat.setReceiveId(3);
        chat.setContent("Hello");
        chat.setTime(LocalDateTime.now());

        when(chatItemMapper.getTheOtherId(anyInt(), anyInt())).thenReturn(3);
        doNothing().when(chatItemMapper).updateChatItem(anyInt(), anyString(), any(LocalDateTime.class));
        doNothing().when(chatMapper).addChat(anyInt(), anyInt(), anyInt(), anyString(), any(LocalDateTime.class));

        chatService.addChat(chat);

        verify(chatItemMapper, times(1)).updateChatItem(anyInt(), anyString(), any(LocalDateTime.class));
        verify(chatMapper, times(1)).addChat(anyInt(), anyInt(), anyInt(), anyString(), any(LocalDateTime.class));
    }

    @Test
    void getAllChatWith() {
        List<Chat> chats = new ArrayList<>();
        Chat chat1 = new Chat();
        chat1.setChatId(1);
        chat1.setSendId(2);
        chat1.setReceiveId(3);
        chat1.setContent("Hello");
        chat1.setTime(LocalDateTime.now());
        chats.add(chat1);

        when(chatMapper.getAllChatWith(anyInt())).thenReturn(chats);

        List<Chat> result = chatService.getAllChatWith(2);

        assertEquals(1, result.size());
        assertEquals(chat1, result.get(0));
        verify(chatMapper, times(1)).getAllChatWith(anyInt());
    }

    @Test
    void getAllChatItem() {
        List<ChatItem> chatItems = new ArrayList<>();
        ChatItem chatItem1 = new ChatItem();
        chatItem1.setId(0);
        chatItem1.setMem1(1);
        chatItem1.setMem2(2);
        chatItem1.setNewestContent("Hello");
        chatItem1.setNewestTime(LocalDateTime.now());
        chatItems.add(chatItem1);

        when(chatItemMapper.getAllChatItem(anyInt())).thenReturn(chatItems);

        List<ChatItem> result = chatService.getAllChatItem(2);

        assertEquals(1, result.size());
        assertEquals(chatItem1, result.get(0));
        verify(chatItemMapper, times(1)).getAllChatItem(anyInt());
    }

    @Test
    void getTheOtherId() {
        when(chatItemMapper.getTheOtherId(anyInt(), anyInt())).thenReturn(3);

        Integer result = chatService.getTheOtherId(1, 2);

        assertEquals(3, result);
        verify(chatItemMapper, times(1)).getTheOtherId(anyInt(), anyInt());
    }
}

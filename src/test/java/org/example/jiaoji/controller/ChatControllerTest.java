package org.example.jiaoji.controller;

import org.example.jiaoji.pojo.Chat;
import org.example.jiaoji.pojo.ChatItem;
import org.example.jiaoji.pojo.ChatItemResponse;
import org.example.jiaoji.service.ChatService;
import org.example.jiaoji.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addChat() {
        Chat chat = new Chat();
        chatController.addChat(chat);

        assertNotNull(chat.getTime()); // Check if time was set
        verify(chatService, times(1)).addChat(chat);
    }

    @Test
    void getAllChatWith() {
        List<Chat> mockChats = new ArrayList<>();
        when(chatService.getAllChatWith(anyInt())).thenReturn(mockChats);

        List<Chat> response = chatController.getAllChatWith(1);
        assertEquals(mockChats, response);

        verify(chatService, times(1)).getAllChatWith(anyInt());
    }

    @Test
    void getAllChatItem() {
        List<ChatItem> mockChatItems = new ArrayList<>();
        ChatItem chatItem = new ChatItem();
        chatItem.setId(1);
        chatItem.setNewestContent("Hello");
        chatItem.setNewestTime(LocalDateTime.now());
        chatItem.setMem1(1);
        chatItem.setMem2(2);

        ChatItem chatItem1 = new ChatItem();
        chatItem1.setId(2);
        chatItem1.setNewestContent("Hello");
        chatItem1.setNewestTime(LocalDateTime.now());
        chatItem1.setMem1(3);
        chatItem1.setMem2(1);
        mockChatItems.add(chatItem);
        mockChatItems.add(chatItem1);

        when(chatService.getAllChatItem(anyInt())).thenReturn(mockChatItems);
        when(userService.getAvater(anyInt())).thenReturn("avatar_url");
        when(userService.getUserName(anyInt())).thenReturn("User Name");

        ResponseEntity<List<ChatItemResponse>> response = chatController.getAllChatItem(1);

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Hello", response.getBody().get(0).getContent());

        verify(chatService, times(1)).getAllChatItem(anyInt());
        verify(userService, times(2)).getAvater(anyInt());
        verify(userService, times(2)).getUserName(anyInt());
    }

    @Test
    void getAvatar() {
        when(userService.getAvater(anyInt())).thenReturn("avatar_url");

        String response = chatController.getAvatar(1);
        assertEquals("avatar_url", response);

        verify(userService, times(1)).getAvater(anyInt());
    }

    @Test
    void getUserId() {
        when(chatService.getTheOtherId(anyInt(), anyInt())).thenReturn(2);

        Integer response = chatController.getUserId(1, 1);
        assertEquals(2, response);

        verify(chatService, times(1)).getTheOtherId(anyInt(), anyInt());
    }
}

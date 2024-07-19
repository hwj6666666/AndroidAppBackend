package org.example.jiaoji.controller;

import org.example.jiaoji.pojo.Chat;
import org.example.jiaoji.pojo.ChatItem;
import org.example.jiaoji.pojo.ChatItemResponse;
import org.example.jiaoji.service.ChatService;
import org.example.jiaoji.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@CrossOrigin
class ChatControllerTest {

    @InjectMocks
    private ChatController chatController;

    @Mock
    private ChatService chatService;

    @Mock
    private UserService userService;

    @Test
    void addChat() {
        MockitoAnnotations.openMocks(this);
        Chat chat = new Chat();
        chatController.addChat(chat);
        assertEquals(LocalDateTime.now().getDayOfYear(), chat.getTime().getDayOfYear());
        verify(chatService, times(1)).addChat(chat);
    }

    @Test
    void getAllChatWith() {
        MockitoAnnotations.openMocks(this);
        List<Chat> chats = Arrays.asList(new Chat(), new Chat());
        when(chatService.getAllChatWith(anyInt())).thenReturn(chats);

        List<Chat> result = chatController.getAllChatWith(1);
        assertEquals(2, result.size());
        verify(chatService, times(1)).getAllChatWith(1);
    }

    @Test
    void getAllChatItem() {
        MockitoAnnotations.openMocks(this);
        ChatItem chatItem1 = new ChatItem();
        chatItem1.setId(1);
        chatItem1.setMem1(1);
        chatItem1.setMem2(2);
        chatItem1.setNewestContent("Hello");
        chatItem1.setNewestTime(LocalDateTime.now());

        ChatItem chatItem2 = new ChatItem();
        chatItem2.setId(2);
        chatItem2.setMem1(3);
        chatItem2.setMem2(1);
        chatItem2.setNewestContent("Hi");
        chatItem2.setNewestTime(LocalDateTime.now());

        List<ChatItem> chatItems = Arrays.asList(chatItem1, chatItem2);
        when(chatService.getAllChatItem(anyInt())).thenReturn(chatItems);
        when(userService.getAvater(anyInt())).thenReturn("avatar");
        when(userService.getUserName(anyInt())).thenReturn("user");

        ResponseEntity<List<ChatItemResponse>> response = chatController.getAllChatItem(1);
        List<ChatItemResponse> responses = response.getBody();

        assertEquals(2, responses.size());
        verify(chatService, times(1)).getAllChatItem(1);
        verify(userService, times(2)).getAvater(anyInt());
        verify(userService, times(2)).getUserName(anyInt());
    }

    @Test
    void getChatItem() {
        MockitoAnnotations.openMocks(this);
        ChatItemResponse chatItemResponse = new ChatItemResponse();
        chatItemResponse.setId(1);
        chatItemResponse.setContent("Hello");
        chatItemResponse.setTime(LocalDateTime.now());

        when(chatService.getChatItem(anyInt(), anyInt())).thenReturn(chatItemResponse);

        ResponseEntity<ChatItemResponse> response = chatController.getChatItem(1, 2);

        assertEquals(chatItemResponse, response.getBody());
        verify(chatService, times(1)).getChatItem(1, 2);
    }

    @Test
    void getAvatar() {
        MockitoAnnotations.openMocks(this);
        when(userService.getAvater(anyInt())).thenReturn("avatar");

        String avatar = chatController.getAvatar(1);

        assertEquals("avatar", avatar);
        verify(userService, times(1)).getAvater(1);
    }

    @Test
    void getUserId() {
        MockitoAnnotations.openMocks(this);
        when(chatService.getTheOtherId(anyInt(), anyInt())).thenReturn(2);

        Integer otherId = chatController.getUserId(1, 1);

        assertEquals(2, otherId);
        verify(chatService, times(1)).getTheOtherId(1, 1);
    }
}

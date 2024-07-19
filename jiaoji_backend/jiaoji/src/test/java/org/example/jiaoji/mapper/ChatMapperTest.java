package org.example.jiaoji.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.jiaoji.mapper.ChatMapper;
import org.example.jiaoji.pojo.Chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ChatMapperTest {

    @Mock
    private ChatMapper chatMapper;

    @Test
    void testAddChat() {
        int chatId = 1;
        int sendId = 2;
        int receiveId = 3;
        String content = "Hello, world!";
        LocalDateTime time = LocalDateTime.now();

        Mockito.doNothing().when(chatMapper).addChat(chatId, sendId, receiveId, content, time);
        chatMapper.addChat(chatId, sendId, receiveId, content, time);
        Mockito.verify(chatMapper).addChat(chatId, sendId, receiveId, content, time);
    }

    @Test
    void testGetAllChatWith() {
        int chatId = 1;
        List<Chat> expected = new ArrayList<>();
        Chat chat = new Chat();
        chat.setChatId(2);
        chat.setSendId(2);
        chat.setReceiveId(1);
        chat.setContent("Hello");
        chat.setTime(LocalDateTime.now());
        Chat chat1 = new Chat();
        chat1.setChatId(1);
        chat1.setSendId(2);
        chat1.setReceiveId(3);
        chat1.setContent("Hello");
        chat1.setTime(LocalDateTime.now());
        expected.add(chat1);

        Mockito.when(chatMapper.getAllChatWith(chatId)).thenReturn(expected);
        List<Chat> actual = chatMapper.getAllChatWith(chatId);
        assertEquals(expected, actual);
    }
}
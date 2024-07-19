package org.example.jiaoji.mapper;

import org.example.jiaoji.pojo.ChatItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatItemMapperTest {

    @Mock
    private ChatItemMapper chatItemMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetChatItemId() {
        when(chatItemMapper.getChatItemId(1, 2)).thenReturn(100);
        when(chatItemMapper.getChatItemId(2, 1)).thenReturn(100);

        int chatItemId = chatItemMapper.getChatItemId(1, 2);
        assertEquals(100, chatItemId);

        chatItemId = chatItemMapper.getChatItemId(2, 1);
        assertEquals(100, chatItemId);

        verify(chatItemMapper, times(2)).getChatItemId(anyInt(), anyInt());
    }

    @Test
    void testAddChatItem() {
        LocalDateTime time = LocalDateTime.now();
        chatItemMapper.addChatItem(1, 2, "Hello", time);

        verify(chatItemMapper, times(1)).addChatItem(1, 2, "Hello", time);
    }

    @Test
    void testUpdateChatItem() {
        LocalDateTime time = LocalDateTime.now();
        chatItemMapper.updateChatItem(100, "Updated content", time);

        verify(chatItemMapper, times(1)).updateChatItem(100, "Updated content", time);
    }

    @Test
    void testGetAllChatItem() {
        List<ChatItem> chatItems = new ArrayList<>();
        ChatItem chatItem = new ChatItem();
        chatItem.setId(1);
        chatItem.setMem1(2);
        chatItem.setMem2(3);
        chatItem.setNewestContent("Content 1");
        chatItem.setNewestTime(LocalDateTime.now());
        chatItems.add(chatItem);
        ChatItem chatItem1 = new ChatItem();
        chatItem1.setId(2);
        chatItem1.setMem1(3);
        chatItem1.setMem2(4);
        chatItem1.setNewestContent("Content 2");
        chatItem1.setNewestTime(LocalDateTime.now());
        chatItems.add(chatItem1);

        when(chatItemMapper.getAllChatItem(1)).thenReturn(chatItems);

        List<ChatItem> result = chatItemMapper.getAllChatItem(1);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(chatItemMapper, times(1)).getAllChatItem(1);
    }

    @Test
    void testGetTheOtherId() {
        when(chatItemMapper.getTheOtherId(100, 1)).thenReturn(2);
        when(chatItemMapper.getTheOtherId(100, 2)).thenReturn(1);

        int otherId = chatItemMapper.getTheOtherId(100, 1);
        assertEquals(2, otherId);

        otherId = chatItemMapper.getTheOtherId(100, 2);
        assertEquals(1, otherId);

        verify(chatItemMapper, times(2)).getTheOtherId(anyInt(), anyInt());
    }

    @Test
    void testGetChatItem() {
        ChatItem chatItem = new ChatItem();
        chatItem.setId(1);
        chatItem.setMem1(2);
        chatItem.setMem2(3);
        chatItem.setNewestContent("Content 1");
        chatItem.setNewestTime(LocalDateTime.now());
        when(chatItemMapper.getChatItem(1, 2)).thenReturn(chatItem);
        when(chatItemMapper.getChatItem(2, 1)).thenReturn(chatItem);

        ChatItem result = chatItemMapper.getChatItem(1, 2);
        assertNotNull(result);
        assertEquals(2, result.getMem1());
        assertEquals(3, result.getMem2());

        result = chatItemMapper.getChatItem(2, 1);
        assertNotNull(result);
        assertEquals(2, result.getMem1());
        assertEquals(3, result.getMem2());

        verify(chatItemMapper, times(2)).getChatItem(anyInt(), anyInt());
    }
}
package org.example.jiaoji.controller;

import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.top3Object;
import org.example.jiaoji.service.ObjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectControllerTest {

    @Mock
    private ObjectService objectService;

    @InjectMocks
    private ObjectController objectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getObject() {
        List<Objects> mockObjects = new ArrayList<>();
        Objects item = new Objects();
        item.setId(1);

        Objects item1 = new Objects();
        item1.setId(2);
        mockObjects.add(item);
        mockObjects.add(item1);
        when(objectService.SelectAllInTopic(anyInt())).thenReturn(mockObjects);
        when(objectService.getAveScore(anyInt())).thenReturn(4.5);
        when(objectService.getHottestRemark(anyInt())).thenReturn("Hot Remark");

        ResponseEntity<List<Objects>> response = objectController.getObject(1);
        assertEquals(mockObjects, response.getBody());

        verify(objectService, times(1)).SelectAllInTopic(anyInt());
        verify(objectService, times(mockObjects.size())).getAveScore(anyInt());
        verify(objectService, times(mockObjects.size())).getHottestRemark(anyInt());
    }

    @Test
    void insert() {
        Objects mockObject = new Objects();
        when(objectService.InsertObject(any(Objects.class))).thenReturn(1);

        Integer response = objectController.insert(mockObject);
        assertEquals(1, response);

        verify(objectService, times(1)).InsertObject(any(Objects.class));
    }

    @Test
    void getObjectById() {
        List<Objects> mockObjects = new ArrayList<>();
        Objects item = new Objects();
        item.setId(1);

        Objects item1 = new Objects();
        item1.setId(2);
        mockObjects.add(item);
        mockObjects.add(item1);
        when(objectService.SelectById(anyInt())).thenReturn(mockObjects);
        when(objectService.getAveScore(anyInt())).thenReturn(4.5);
        when(objectService.getHottestRemark(anyInt())).thenReturn("Hot Remark");

        ResponseEntity<List<Objects>> response = objectController.getObjectById(1);
        assertEquals(mockObjects, response.getBody());

        verify(objectService, times(1)).SelectById(anyInt());
        verify(objectService, times(mockObjects.size())).getAveScore(anyInt());
        verify(objectService, times(mockObjects.size())).getHottestRemark(anyInt());
    }

    @Test
    void getObjectsByTopicId() {
        List<Objects> mockObjects = Arrays.asList(new Objects(), new Objects());
        when(objectService.search(anyString())).thenReturn(mockObjects);

        List<Objects> response = objectController.getObjectsByTopicId("keyword");
        assertEquals(mockObjects, response);

        verify(objectService, times(1)).search(anyString());
    }

    @Test
    void testGetObjectsByTopicId() {
        List<top3Object> mockTop3Objects = Arrays.asList(new top3Object(), new top3Object());
        when(objectService.SelectTop3(anyInt())).thenReturn(mockTop3Objects);

        List<top3Object> response = objectController.getObjectsByTopicId(1);
        assertEquals(mockTop3Objects, response);

        verify(objectService, times(1)).SelectTop3(anyInt());
    }

    @Test
    void delete() {
        RetType mockRetType = new RetType();
        when(objectService.deleteObject(anyInt())).thenReturn(mockRetType);

        RetType response = objectController.delete(1);
        assertEquals(mockRetType, response);

        verify(objectService, times(1)).deleteObject(anyInt());
    }
}

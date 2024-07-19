package org.example.jiaoji.controller;

import org.example.jiaoji.pojo.Class;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ClassControllerTest {

    @InjectMocks
    private ClassController classController;

    @Mock
    private ClassService classService;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetMethodName() {
        List<Class> classes = Arrays.asList(new Class(), new Class());
        String jsonClasses = JSON.toJSONString(classes);

        when(valueOperations.get("class")).thenReturn(null);
        when(classService.selectAll()).thenReturn(classes);

        List<Class> result = classController.getMethodName();
        assertEquals(classes, result);
        verify(valueOperations, times(1)).get("class");
        verify(classService, times(1)).selectAll();
        verify(valueOperations, times(1)).set("class", jsonClasses, 3600, TimeUnit.SECONDS);

        // Test when cache is not null
        when(valueOperations.get("class")).thenReturn(jsonClasses);

        result = classController.getMethodName();
        assertEquals(classes, result);
        verify(valueOperations, times(3)).get("class");
        verify(classService, times(1)).selectAll();  // Only called once from the first test case
        verify(valueOperations, times(1)).set("class", jsonClasses, 3600, TimeUnit.SECONDS);  // Only called once from the first test case
    }

    @Test
    void testGetMethodName2() {
        RetType retType = new RetType();
        when(classService.addFavor(anyInt(), anyInt())).thenReturn(retType);

        RetType result = classController.getMethodName2(1, 1);
        assertEquals(retType, result);
        verify(classService, times(1)).addFavor(1, 1);
    }

    @Test
    void testGetMethodName3() {
        RetType retType = new RetType();
        when(classService.deleteFavor(anyInt(), anyInt())).thenReturn(retType);

        RetType result = classController.getMethodName3(1, 1);
        assertEquals(retType, result);
        verify(classService, times(1)).deleteFavor(1, 1);
    }

    @Test
    void testGetMethodName4() {
        List<Integer> favorList = Arrays.asList(1, 2, 3);
        when(classService.selectFavor(anyInt())).thenReturn(favorList);

        List<Integer> result = classController.getMethodName4(1);
        assertEquals(favorList, result);
        verify(classService, times(1)).selectFavor(1);
    }
}

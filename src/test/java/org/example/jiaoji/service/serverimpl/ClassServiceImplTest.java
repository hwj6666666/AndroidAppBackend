package org.example.jiaoji.service.serverimpl;

import org.example.jiaoji.mapper.ClassMapper;
import org.example.jiaoji.pojo.Class;
import org.example.jiaoji.pojo.RetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassServiceImplTest {

    @Mock
    private ClassMapper classMapper;

    @InjectMocks
    private ClassServiceImpl classService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void selectAll() {
        List<Class> classes = new ArrayList<>();
        Class class1 = new Class();
        class1.setId(1);
        class1.setName("Math");
        classes.add(class1);

        when(classMapper.selectAll()).thenReturn(classes);

        List<Class> result = classService.selectAll();

        assertEquals(1, result.size());
        assertEquals(class1, result.get(0));
        verify(classMapper, times(1)).selectAll();
    }

    @Test
    void addFavor() {
        when(classMapper.addFavor(anyInt(), anyInt())).thenReturn(1);

        RetType result = classService.addFavor(1, 2);

        assertEquals(true, result.isOk());
        assertEquals("关注成功", result.getMsg());
        verify(classMapper, times(1)).addFavor(anyInt(), anyInt());

        when(classMapper.addFavor(anyInt(), anyInt())).thenReturn(0);

        result = classService.addFavor(1, 2);

        assertEquals(false, result.isOk());
        assertEquals("未知原因，收藏失败，请联系管理员", result.getMsg());
        verify(classMapper, times(2)).addFavor(anyInt(), anyInt());
    }

    @Test
    void deleteFavor() {
        when(classMapper.deleteFavor(anyInt(), anyInt())).thenReturn(1);

        RetType result = classService.deleteFavor(1, 2);

        assertEquals(true, result.isOk());
        assertEquals("取关成功", result.getMsg());
        verify(classMapper, times(1)).deleteFavor(anyInt(), anyInt());

        when(classMapper.deleteFavor(anyInt(), anyInt())).thenReturn(0);

        result = classService.deleteFavor(1, 2);

        assertEquals(false, result.isOk());
        assertEquals("未知原因，取消收藏失败，请联系管理员", result.getMsg());
        verify(classMapper, times(2)).deleteFavor(anyInt(), anyInt());
    }

    @Test
    void selectFavor() {
        List<Integer> favorList = new ArrayList<>();
        favorList.add(1);
        favorList.add(2);

        when(classMapper.selectFavor(anyInt())).thenReturn(favorList);

        List<Integer> result = classService.selectFavor(2);

        assertEquals(2, result.size());
        assertEquals(favorList, result);
        verify(classMapper, times(1)).selectFavor(anyInt());
    }
}

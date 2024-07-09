package org.example.jiaoji.service;


import org.example.jiaoji.pojo.Class;
import org.example.jiaoji.pojo.RetType;

import java.util.List;

public interface ClassService {
    public List<Class> selectAll();

    public RetType addFavor(Integer classId, Integer userId);

    public RetType deleteFavor(Integer classId, Integer userId);

    public List<Integer> selectFavor(Integer userId);
}

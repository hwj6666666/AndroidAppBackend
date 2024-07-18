package org.example.jiaoji.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetType {
    private boolean ok;
    private String msg;
    private Object data;    //Object为java中所有类的父类,可以接收任意类型的数据对象
}

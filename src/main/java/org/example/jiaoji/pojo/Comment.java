package org.example.jiaoji.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
//针对对象的直接评论
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer remarkId;
    private String content;
    private LocalDateTime publishTime;
}

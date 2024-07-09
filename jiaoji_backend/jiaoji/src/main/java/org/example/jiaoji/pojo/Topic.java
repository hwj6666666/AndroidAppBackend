package org.example.jiaoji.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    private Integer id;
    private Integer classId;//详见前端页面
    private Integer userId;
    private String title;
    private String picture;
    private String introduction;
    private Integer hot;//热度，将子话题的评论量累加
    private LocalDateTime publicTime;
    private String base64;
    private Integer views;
    private Integer remarkNum;
    private Integer favor;
    private Integer objectNum;
}

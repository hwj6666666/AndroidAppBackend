package org.example.jiaoji.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
//针对对象的直接评论
public class Remark {
    private Integer id;
    private Integer userId;
    private Integer objectId;

    private String content;
    private Integer like;
    private Integer score;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishTime;
}
package org.example.jiaoji.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

//针对对象的直接评论

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Objects {
    private Integer id;
    private Integer topicId;
    private Integer userId;
    private String title;
    private String description;
    private String picture;
    private String hottestRemark;
    private double aveScore;
}

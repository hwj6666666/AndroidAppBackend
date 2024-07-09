package org.example.jiaoji.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarkLike {
    private Integer id;
    private Integer uid;
    private Integer remarkId;
    private Boolean liked;
}

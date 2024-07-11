package org.example.jiaoji.pojo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatItem {
    private Integer id;
    private Integer mem1;
    private Integer mem2;
    private String newestContent;
    private LocalDateTime newestTime;
}

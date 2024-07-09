package org.example.jiaoji.pojo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatItemResponse {
  private Integer id;
  private String content;
  private LocalDateTime time;
  private String name;
  private String avatar;
}

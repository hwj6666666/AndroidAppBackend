package org.example.jiaoji.pojo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Chat {
  private Integer chatId;
  private Integer sendId;
  private Integer receiveId;
  private String content;
  private LocalDateTime time;

  @Override
  public String toString() {
    return "Chat{"
        + "sendId="
        + sendId
        + ", receiveId="
        + receiveId
        + ", content='"
        + content
        + '\''
        + ", time="
        + time
        + "chatId="
        + chatId
        + '}';
  }
}

package org.example.jiaoji.mapper;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.jiaoji.pojo.Chat;
import org.example.jiaoji.pojo.ChatItem;

@Mapper
public interface ChatMapper {

  @Insert(
      "INSERT INTO chat(chat_id, send_id, receive_id, content, time) VALUES(#{chatId}, #{sendId}, #{receiveId}, #{content}, #{time})")
  void addChat(
      Integer chatId, Integer sendId, Integer receiveId, String content, LocalDateTime time);

  @Select("SELECT * FROM chat WHERE chat_id=#{chatId}")
  List<Chat> getAllChatWith(Integer chatId);


}

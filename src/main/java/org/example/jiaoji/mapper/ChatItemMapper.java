package org.example.jiaoji.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.jiaoji.pojo.ChatItem;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ChatItemMapper {
    @Select("SELECT id FROM chat_item WHERE mem_1=#{sendId} AND mem_2=#{receiveId} OR mem_1=#{receiveId} AND mem_2=#{sendId}")
    Integer getChatItemId(Integer sendId,Integer receiveId);

    @Insert("INSERT INTO chat_item(mem_1, mem_2, newest_content, newest_time) VALUES(#{sendId}, #{receiveId}, #{content}, #{time})")
    void addChatItem(Integer sendId, Integer receiveId, String content, LocalDateTime time);

    @Insert("UPDATE chat_item SET newest_content=#{content}, newest_time=#{time} WHERE id=#{chatItemId}")
    void updateChatItem(Integer chatItemId, String content, LocalDateTime time);

    @Select("SELECT * FROM chat_item WHERE mem_1=#{userId} OR mem_2=#{userId}")
    List<ChatItem> getAllChatItem(Integer userId);


    @Select("SELECT CASE WHEN mem_1=#{sendId} THEN mem_2 WHEN mem_2=#{sendId} THEN mem_1 END FROM chat_item WHERE id=#{chatId}")
    Integer getTheOtherId(Integer chatId, Integer sendId);

    @Select("SELECT * FROM chat_item WHERE (mem_1 = #{userId} AND mem_2 = #{otherId}) OR (mem_1 = #{otherId} AND mem_2 = #{userId})")
    ChatItem getChatItem(Integer userId, Integer otherId);
}

package org.example.jiaoji.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.jiaoji.pojo.Chat;
import org.example.jiaoji.pojo.ChatItem;
import org.example.jiaoji.pojo.ChatItemResponse;
import org.example.jiaoji.service.ChatService;
import org.example.jiaoji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ChatController {

  @Autowired private ChatService chatService;
  @Autowired private UserService userService;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @PostMapping("/chat")
  public void addChat(@RequestBody Chat chat) {
    chat.setTime(LocalDateTime.now());
    chatService.addChat(chat);
  }

  @GetMapping("/chat")
  public List<Chat> getAllChatWith(@RequestParam Integer id) {
    return chatService.getAllChatWith(id);
  }

  @GetMapping("/chat/list")
  public ResponseEntity<List<ChatItemResponse>> getAllChatItem(@RequestParam Integer userId) {

    List<ChatItem> chatItems = chatService.getAllChatItem(userId);
    List<ChatItemResponse> responses = new ArrayList<>();

    for (ChatItem chatItem : chatItems) {
      ChatItemResponse response = new ChatItemResponse();
      response.setId(chatItem.getId());
      response.setContent(chatItem.getNewestContent());
      response.setTime(chatItem.getNewestTime());

      // 获取另一个用户的头像，这里假设你有一个方法可以根据用户ID获取用户的头像
      Integer otherUserId =chatItem.getMem1().equals(userId) ? chatItem.getMem2() : chatItem.getMem1();
      String otherUserAvatar = userService.getAvater(otherUserId);
      response.setAvatar(otherUserAvatar);
      String otherUserName = userService.getUserName(otherUserId);
      response.setName(otherUserName);
      responses.add(response);
    }

    return ResponseEntity.ok(responses);
  }

  @GetMapping("chat/item")
  public ResponseEntity<ChatItemResponse> getChatItem(
      @RequestParam Integer userId, @RequestParam Integer otherId) {

    ChatItemResponse response=chatService.getChatItem(userId,otherId);


    return ResponseEntity.ok(response);
  }

  @GetMapping("/chat/avatar")
  public String getAvatar(@RequestParam Integer userId) {
    String key="user:"+userId+"avater";
    if(stringRedisTemplate.opsForValue().get(key)!=null){
      return stringRedisTemplate.opsForValue().get(key);
    }else{
      String avatar=userService.getAvater(userId);
      stringRedisTemplate.opsForValue().set(key,avatar,3600,java.util.concurrent.TimeUnit.SECONDS);
      return avatar;
    }
  }

  @GetMapping("/chat/id")
  public Integer getUserId(@RequestParam Integer userId, @RequestParam Integer chatId) {
    return chatService.getTheOtherId(chatId, userId);
  }
}

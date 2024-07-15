package com.renyibang.chatapi.controller;

import com.renyibang.chatapi.service.ChatService;
import com.renyibang.global.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
  @Autowired private ChatService chatService;

  @GetMapping("/list")
  public Response getChats(@RequestHeader("userId") long userId) {
    return chatService.getChats(userId);
  }

  @PostMapping("/enter/{type}/{ofId}")
  public Response initiateChat(
      @PathVariable String type, @PathVariable long ofId, @RequestHeader("userId") long userId) {
    return chatService.initiateChat(userId, (byte) (type.equals("task") ? 0 : 1), ofId);
  }

  @GetMapping("/history")
  public Response getChatHistory(
      String chatId, String lastMessageId, int count, @RequestHeader("userId") long userId) {
    return chatService.getChatHistory(userId, chatId, lastMessageId, count);
  }
}

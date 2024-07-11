package com.renyibang.chatapi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.chatapi.dao.ChatRepository;
import com.renyibang.chatapi.dao.MessageRepository;
import com.renyibang.chatapi.entity.Chat;
import com.renyibang.chatapi.entity.Message;
import com.renyibang.chatapi.service.ChatService;
import com.renyibang.global.client.TaskClient;
import com.renyibang.global.client.UserClient;
import com.renyibang.global.util.ResponseUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  @Autowired private ChatRepository chatRepository;
  @Autowired private MessageRepository messageRepository;
  @Autowired private UserClient userClient;
  @Autowired private TaskClient taskClient;

  private JSONObject getChatJson(Chat chat, long userId) {
    long chatterId = chat.getOfOwnerId() == userId ? chat.getChatterId() : chat.getOfOwnerId();
    JSONObject chatterJson = new JSONObject();
    JSONObject res = userClient.getUserInfo(chatterId);
    if (res.getBooleanValue("ok")) chatterJson = res.getJSONObject("data");
    JSONObject chatJson = new JSONObject();
    chatJson.put("chatId", chat.getChatId());
    chatJson.put("type", chat.getType() == 0 ? "task" : "service");
    chatJson.put("unread", userId == chat.getLastMessageSenderId() ? 0 : chat.getUnreadCount());
    chatJson.put("lastMessage", chat.getLastMessageContent());
    chatJson.put("lastTime", chat.getLastMessageCreatedAt().format(formatter));
    chatJson.put("chatter", chatterJson);
    return chatJson;
  }

  private JSONObject getMessageJson(Message message) {
    JSONObject messageJson = new JSONObject();
    messageJson.put("messageId", message.getMessageId());
    messageJson.put("senderId", message.getSenderId());
    messageJson.put("content", message.getContent());
    messageJson.put("time", message.getCreatedAt().format(formatter));
    return messageJson;
  }

  @Override
  public JSONObject getChats(long userId) {
    List<Chat> chats =
        chatRepository.findByOfOwnerIdOrChatterIdOrderByLastMessageCreatedAtDesc(userId, userId);
    chats.sort((a, b) -> b.getLastMessageCreatedAt().compareTo(a.getLastMessageCreatedAt()));
    JSONArray chatArray = new JSONArray();
    for (Chat chat : chats) chatArray.add(getChatJson(chat, userId));
    JSONObject data = new JSONObject();
    data.put("chats", chatArray);
    JSONObject selfJson = new JSONObject();
    JSONObject res = userClient.getUserInfo(userId);
    if (res.getBooleanValue("ok")) selfJson = res.getJSONObject("data");
    data.put("self", selfJson);
    return ResponseUtil.success(data);
  }

  @Override
  public JSONObject initiateChat(long userId, byte type, long ofId) {
    Chat chat = chatRepository.findByTypeAndOfIdAndChatterId(type, ofId, userId).orElse(null);
    if (chat == null) {
      // 服务模块也要加
      JSONObject res = taskClient.getTaskOwnerId(ofId);
      if (!res.getBooleanValue("ok")) return ResponseUtil.error(res.getString("message"));
      long ofOwnerId = res.getLongValue("data");
      chat = new Chat();
      chat.setType(type);
      chat.setOfId(ofId);
      chat.setOfOwnerId(ofOwnerId);
      chat.setChatterId(userId);
      chat.setLastMessageSenderId(userId);
      chatRepository.save(chat);
    }
    JSONObject data = new JSONObject();
    data.put("chatId", chat.getChatId());
    return ResponseUtil.success(data);
  }

  @Override
  public JSONObject getChatHistory(long userId, String chatId, String lastMessageId, int count) {
    Chat chat = chatRepository.findById(chatId).orElse(null);
    if (chat == null) return ResponseUtil.error("聊天不存在");
    if (chat.getOfOwnerId() != userId && chat.getChatterId() != userId)
      return ResponseUtil.error("无权查看聊天记录");
    Message lastMessage = messageRepository.findById(lastMessageId).orElse(null);
    LocalDateTime lastMessageCreatedAt =
        lastMessage == null ? chat.getLastMessageCreatedAt() : lastMessage.getCreatedAt();
    Page<Message> messages =
        messageRepository.findByChatIdAndCreatedAtBeforeOrderByCreatedAtDesc(
            chatId, lastMessageCreatedAt, PageRequest.of(0, count));
    JSONArray messageArray = new JSONArray();
    for (Message message : messages) messageArray.add(getMessageJson(message));
    return ResponseUtil.success(messageArray);
  }
}

package com.renyibang.chatapi.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@Document("message")
public class Message {
  @MongoId private String messageId;
  @Indexed private String chatId;
  private long senderId;
  private String content;

  @CreatedDate
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Indexed
  private LocalDateTime createdAt;

  public Message(String chatId, long senderId, String content) {
    this.chatId = chatId;
    this.senderId = senderId;
    this.content = content;
  }
}

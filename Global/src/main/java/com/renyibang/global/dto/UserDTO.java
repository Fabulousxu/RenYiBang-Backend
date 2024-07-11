package com.renyibang.global.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
  long id;
  long balance;
  String nickname;
  String avatar;
}

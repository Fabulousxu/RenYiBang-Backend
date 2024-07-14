package com.renyibang.global.dto;

import lombok.Data;

@Data
public class UserDTO {
	long id;
	long balance;
	String nickname;
	String avatar;

	public UserDTO(long id, long balance, String nickname, String avatar) {
		this.id = id;
		this.balance = balance;
		this.nickname = nickname;
		this.avatar = avatar;
	}

  public UserDTO() {

  }
}

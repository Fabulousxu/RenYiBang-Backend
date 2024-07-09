package com.renyibang.orderapi.dto;

import lombok.Data;

@Data
public class UserDTO {
	long id;
	long balance;
	long nickname;
	String avatar;
}

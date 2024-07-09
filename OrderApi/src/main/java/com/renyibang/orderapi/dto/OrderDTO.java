package com.renyibang.orderapi.dto;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.orderapi.entity.Order;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class OrderDTO {

	Order order;
	TaskDTO task;
	ServiceDTO service;
	UserDTO owner;
	UserDTO accessor;

	public OrderDTO() {

	}

	public OrderDTO(Order order) {
		this.order = order;
	}

	// Getters and setters

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("initiator_name", initiatorName);
		json.put("initiator_img", initiatorImg);
		json.put("recipient_name", recipientName);
		json.put("recipient_img", recipientImg);
		json.put("status", order.getStatus().getCode());
		json.put("time", time);
		json.put("order_img", orderImg);
		json.put("description", description);
		return json;
	}
}

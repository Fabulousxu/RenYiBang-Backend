package com.renyibang.orderapi.entity;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.orderapi.enums.OrderStatus;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "`order`")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private long orderId;

	@Column(name = "type")
	private Byte type;

	@Column(name = "owner_id")
	private long ownerId;

	@Column(name = "accessor_id")
	private long accessorId;

	@Column(name = "status")
	private OrderStatus status;

	@Column(name = "cost")
	private long cost;

	@Column(name = "item_id")
	private long itemId; // task_id or service_id

	// 有问题
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		json.put("id", orderId);
		json.put("type", type);
		json.put("initiator", owner.getNickname());
		json.put("recipient", accessor.getNickname());
		json.put("status", status.getCode());
		json.put("cost", cost);
		json.put("time", item.getCreatedAt());
		json.put("name", item.getTitle());
		return json;
	}
}

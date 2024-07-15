package com.renyibang.orderapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.dto.ServiceDTO;
import com.renyibang.global.dto.TaskDTO;
import com.renyibang.global.dto.UserDTO;
import com.renyibang.global.util.ResponseUtil;
import com.renyibang.orderapi.dto.OrderDTO;
import com.renyibang.orderapi.entity.Order;
import com.renyibang.orderapi.enums.OrderStatus;
import com.renyibang.orderapi.service.OrderService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import okhttp3.internal.concurrent.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {
	private MockMvc mockMvc;

	@Mock
	private OrderService orderService;

	@InjectMocks
	private OrderController orderController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
	}

	@Test
	@DisplayName("Should create task order successfully")
	public void createTaskOrderSuccessfully() throws Exception {
		when(orderService.createOrder(anyLong(), anyLong(), anyLong(), anyLong(), any(Byte.class))).thenReturn(true);

		mockMvc.perform(post("/api/order/task/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"taskId\":1,\"ownerId\":1,\"accessorId\":2,\"cost\":100}")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(true))
				.andExpect(jsonPath("$.message").value("订单创建成功"));
	}

	@Test
	@DisplayName("Should fail to create task order due to mismatched ownerId")
	public void failToCreateTaskOrderDueToMismatchedOwnerId() throws Exception {
		mockMvc.perform(post("/api/order/task/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"taskId\":1,\"ownerId\":2,\"accessorId\":2,\"cost\":100}")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(false))
				.andExpect(jsonPath("$.message").value("ownerId不匹配"));
	}

	@Test
	@DisplayName("Should fail to create task order due to exception")
	public void failToCreateTaskOrderDueToException() throws Exception {
		when(orderService.createOrder(anyLong(), anyLong(), anyLong(), anyLong(), any(Byte.class))).thenThrow(new RuntimeException("订单创建失败"));

		mockMvc.perform(post("/api/order/task/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"taskId\":1,\"ownerId\":1,\"accessorId\":2,\"cost\":100}")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(false))
				.andExpect(jsonPath("$.message").value("订单创建失败"));
	}

	@Test
	@DisplayName("Should get order successfully")
	public void getOrderSuccessfully() throws Exception {
		TaskDTO taskDTO = new TaskDTO(1, "task", "task description", "test.jpg", LocalDateTime.of(2021, 1, 1, 0, 0));
		UserDTO owner = new UserDTO(1, 10000, "owner", "owner.jpg");
		UserDTO accessor = new UserDTO(2, 10000, "accessor", "accessor.jpg");
		Order order = new Order(1, (byte) 0, 1, 2, OrderStatus.UNPAID, 100, 1);

		OrderDTO orderDTO = new OrderDTO(order);
		orderDTO.setTask(taskDTO);
		orderDTO.setOwner(owner);
		orderDTO.setAccessor(accessor);

		when(orderService.findById(anyLong())).thenReturn(orderDTO);

		mockMvc.perform(get("/api/order/1")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(true))
				.andExpect(jsonPath("$.data").exists());
	}

	@Test
	@DisplayName("Should fail to get order due to order not found")
	public void failToGetOrderDueToOrderNotFound() throws Exception {
		when(orderService.findById(anyLong())).thenReturn(null);

		mockMvc.perform(get("/api/order/1")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(false))
				.andExpect(jsonPath("$.message").value("订单不存在"));
	}

	@Test
	@DisplayName("Should get task order by owner successfully")
	public void getTaskOrderByOwnerSuccessfully() throws Exception {
		mockMvc.perform(get("/api/order/task/initiator/1")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(true))
				.andExpect(jsonPath("$.data").exists());
	}

	@Test
	@DisplayName("Should get task order by accessor successfully")
	public void getTaskOrderByAccessorSuccessfully() throws Exception {
		mockMvc.perform(get("/api/order/task/recipient/1")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(true))
				.andExpect(jsonPath("$.data").exists());
	}

	@Test
	@DisplayName("Should get service order by owner successfully")
	public void getServiceOrderByOwnerSuccessfully() throws Exception {
		mockMvc.perform(get("/api/order/service/initiator/1")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(true))
				.andExpect(jsonPath("$.data").exists());
	}

	@Test
	@DisplayName("Should get service order by accessor successfully")
	public void getServiceOrderByAccessorSuccessfully() throws Exception {
		mockMvc.perform(get("/api/order/service/recipient/1")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(true))
				.andExpect(jsonPath("$.data").exists());
	}

	@Test
	@DisplayName("Should change task order status successfully")
	public void changeTaskOrderStatusSuccessfully() throws Exception {
		when(orderService.markOrderStatus(anyLong(), anyLong(), any(OrderStatus.class))).thenReturn(
				Pair.of(true, "订单状态更新成功"));

		mockMvc.perform(post("/api/order/task/status")
						.param("orderId", "1")
						.param("status", "COMPLETED")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(true))
				.andExpect(jsonPath("$.message").value("订单状态修改成功"));
	}

	@Test
	@DisplayName("Should fail to change task order status due to invalid status")
	public void failToChangeTaskOrderStatusDueToInvalidStatus() throws Exception {
		mockMvc.perform(post("/api/order/task/status")
						.param("orderId", "1")
						.param("status", "INVALID")
						.header("userId", 1))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Should fail to change task order status, false return by service")
	public void failToChangeTaskOrderStatusDueToInvalidOrderId() throws Exception {
		when(orderService.markOrderStatus(anyLong(), anyLong(), any(OrderStatus.class))).thenReturn(
				Pair.of(false, "订单状态更新失败"));

		mockMvc.perform(post("/api/order/task/status")
						.param("orderId", "1")
						.param("status", "COMPLETED")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(false))
				.andExpect(jsonPath("$.message").value("订单状态更新失败"));
	}

	@Test
	@DisplayName("Should fail to change service order status due to invalid orderId")
	public void failToChangeServiceOrderStatusDueToInvalidOrderId() throws Exception {
		when(orderService.markOrderStatus(anyLong(), anyLong(), any(OrderStatus.class))).thenReturn(
				Pair.of(false, "订单状态更新失败"));

		mockMvc.perform(post("/api/order/service/status")
						.param("orderId", "1")
						.param("status", "COMPLETED")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(false))
				.andExpect(jsonPath("$.message").value("订单状态更新失败"));
	}

	@Test
	@DisplayName("Should fail to change service order status due to invalid status")
	public void failToChangeServiceOrderStatusDueToInvalidStatus() throws Exception {
		mockMvc.perform(post("/api/order/service/status")
						.param("orderId", "1")
						.param("status", "INVALID")
						.header("userId", 1))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Should change service order status successfully")
	public void changeServiceOrderStatusSuccessfully() throws Exception {
		when(orderService.markOrderStatus(anyLong(), anyLong(), any(OrderStatus.class))).thenReturn(
				Pair.of(true, "订单状态更新成功"));

		mockMvc.perform(post("/api/order/service/status")
						.param("orderId", "1")
						.param("status", "COMPLETED")
						.header("userId", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ok").value(true))
				.andExpect(jsonPath("$.message").value("订单状态修改成功"));
	}

}
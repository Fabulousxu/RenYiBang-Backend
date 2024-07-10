package com.renyibang.orderapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.feignclient.TaskClient;
import com.renyibang.orderapi.dto.OrderDTO;
import com.renyibang.orderapi.entity.Order;
import com.renyibang.orderapi.enums.OrderStatus;
import com.renyibang.orderapi.service.OrderService;
import com.renyibang.orderapi.util.ResponseUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
  @Autowired
  private OrderService orderService;

  // 生成订单
  // /task/create
  // Long taskId, Long ownerId, Long accessorId, Long cost
  // token中的userId
  @PostMapping("/task/create")
  public JSONObject createTaskOrder(@RequestBody JSONObject data) {
    // TODO: token, 获取当前用户id
    // 测试用: userId = 1
    int userId = 1;

    long taskId = data.getLong("taskId");
    long ownerId = data.getLong("ownerId");
    long accessorId = data.getLong("accessorId");
    long cost = data.getLong("cost");
    // 校验ownerId是否为当前用户
    if (ownerId != userId) {
      return ResponseUtil.error("ownerId不匹配");
    }

    // 创建订单
    orderService.createOrder(taskId, ownerId, accessorId, cost, (byte) 0);
    return ResponseUtil.success("订单创建成功");
  }

  // 获取指定id的order信息
  // /api/order/{id}
  @GetMapping("/{id}")
  public JSONObject getOrder(@PathVariable Long id) {
    // TODO: token, 获取当前用户id
    // 身份校验
    OrderDTO order = orderService.findById(id);
    return ResponseUtil.success(order.getDetail());
  }

  // /task/owner/:ownerId
  @GetMapping("/task/initiator/{ownerId}")
  public JSONObject getTaskOrderByOwner(@PathVariable Long ownerId) {
    // TODO：取消路径参数，使用token获取用户id
    List<OrderDTO> taskOrders = orderService.findByOwnerIdAndType(ownerId, (byte) 0);
    return ResponseUtil.success(toJSON(taskOrders));
  }

  @GetMapping("/task/recipient/{accessorId}")
  public JSONObject getTaskOrderByAccessor(@PathVariable Long accessorId) {
    // TODO：取消路径参数，使用token获取用户id
    List<OrderDTO> taskOrders = orderService.findByAccessorIdAndType(accessorId, (byte) 0);
    return ResponseUtil.success(toJSON(taskOrders));
  }
  @GetMapping("/service/initiator/{ownerId}")
  public JSONObject getServiceOrderByOwner(@PathVariable Long ownerId) {
    // TODO：取消路径参数，使用token获取用户id
    List<OrderDTO> serviceOrders = orderService.findByOwnerIdAndType(ownerId, (byte) 1);
    return ResponseUtil.success(toJSON(serviceOrders));
  }
  @GetMapping("/service/recipient/{accessorId}")
  public JSONObject getServiceOrderByAccessor(@PathVariable Long accessorId) {
    // TODO：取消路径参数，使用token获取用户id
    List<OrderDTO> serviceOrders = orderService.findByAccessorIdAndType(accessorId, (byte) 1);
    return ResponseUtil.success(toJSON(serviceOrders));
  }

  // 任务完成
  @PostMapping("/task/status")
  public JSONObject changeTaskOrderStatus(@RequestParam Long orderId, @RequestParam OrderStatus status) {
    // TODO: token, 获取当前用户id
    // 测试用: userId = 1
    int userId = 1;

    Pair<Boolean, String> result = orderService.markOrderStatus(orderId, userId, status);

    if (result.getFirst()) {
      return ResponseUtil.success("订单状态修改成功");
    } else {
      return ResponseUtil.error(result.getSecond());
    }
  }

  @PostMapping("/service/status")
  public JSONObject changeServiceOrderStatus(@RequestParam Long orderId, @RequestParam OrderStatus status) {
    // TODO: token, 获取当前用户id
    // 测试用: userId = 1
    int userId = 1;

    Pair<Boolean, String> result = orderService.markOrderStatus(orderId, userId, status);

    if (result.getFirst()) {
      return ResponseUtil.success("订单状态修改成功");
    } else {
      return ResponseUtil.error(result.getSecond());
    }
  }

  // 非接口
  // 传入List<Order>，对于每一个Order，调用toJSON()方法，返回List<JSONObject>
  // 使用模板
  private List<JSONObject> toJSON(List<? extends OrderDTO> orders) {
    List<JSONObject> jsonObjects = new ArrayList<>();
    for (OrderDTO order : orders) {
      jsonObjects.add(order.toJSON());
    }
    return jsonObjects;
  }
}

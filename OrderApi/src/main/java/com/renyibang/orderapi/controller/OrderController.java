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
  @GetMapping("/service/initiator")
  public JSONObject getServiceOrderByOwner() {
    // TODO：取消路径参数，使用token获取用户id
    List<OrderDTO> serviceOrders = orderService.findByOwnerIdAndType(1, (byte) 1);
    return ResponseUtil.success(toJSON(serviceOrders));
  }
  @GetMapping("/service/recipient")
  public JSONObject getServiceOrderByAccessor() {
    // TODO：取消路径参数，使用token获取用户id
    List<OrderDTO> serviceOrders = orderService.findByAccessorIdAndType(1, (byte) 1);
    return ResponseUtil.success(toJSON(serviceOrders));
  }

  // 支付订单
  // ？怎么实现？不同的支付api?
  // 设计模式？
  @PostMapping("/task/pay/{orderId}")
  public JSONObject payTaskOrder(@PathVariable Long orderId) {
    // TODO: token, 获取当前用户id
    // TODO：支付api
    int userId = 1;
    // 校验用户是否为任务发布者
    OrderDTO taskOrder = orderService.findById(orderId);
    if (userId != taskOrder.getOwner().getId()) {
      return ResponseUtil.error("该用户不是任务发布者");
    }

    // 支付订单
    orderService.payOrder(taskOrder.getOrder());
    return ResponseUtil.success("支付成功");
  }

  @PostMapping("/service/pay/{orderId}")
  public JSONObject payServiceOrder(@PathVariable Long orderId) {
    return ResponseUtil.success("支付成功");
  }

  // 任务完成
  @PostMapping("/task/status")
  public JSONObject completeTaskOrder(@RequestParam Long orderId, @RequestParam OrderStatus status) {
    // TODO: token, 获取当前用户id
    // 测试用: userId = 1
    int userId = 1;

    // 已知orderId, 获取order
    OrderDTO taskOrder = orderService.findById(orderId);
    // 校验用户是否为任务接收者
    if(taskOrder.getAccessor().getId() != userId) {
      return ResponseUtil.error("该用户不是任务接收者");
    }
//    // 校验任务状态是否为进行中
//    if (taskOrder.getStatus() != TaskStatus.IN_PROGRESS) {
//      return ResponseUtil.error("任务状态错误：未进行中");
//    }
    // 修改订单状态
//    taskOrder.setStatus(TaskStatus.COMPLETED);
    taskOrder.getOrder().setStatus(status);
    return ResponseUtil.success("修改订单状态成功");
  }

  @PostMapping("/service/status")
  public JSONObject completeServiceOrder(@RequestParam Long orderId, @RequestParam OrderStatus status) {
    // TODO: token, 获取当前用户id
    // 测试用: userId = 1
    int userId = 1;

    // 已知orderId, 获取order
    OrderDTO serviceOrder = orderService.findById(orderId);
    // 校验用户是否为任务接收者
    if(serviceOrder.getAccessor().getId() != userId) {
      return ResponseUtil.error("该用户不是任务接收者");
    }
    // 修改订单状态
    serviceOrder.getOrder().setStatus(status);
    return ResponseUtil.success("修改订单状态成功");
  }

  // 确认订单完成
  @PostMapping("/task/confirm/{orderId}")
  public JSONObject confirmTaskOrder(@PathVariable Long orderId) {
    // TODO: token, 获取当前用户id
    // 测试用: userId = 1
    int userId = 3;

    // 已知orderId, 获取order
    OrderDTO taskOrder = orderService.findById(orderId);
    // 校验用户是否为任务发起者
    if (taskOrder.getOwner().getId() != userId) {
      return ResponseUtil.error("该用户不是任务发起者");
    }
    // 校验任务状态是否为已完成
    if (taskOrder.getOrder().getStatus() != OrderStatus.COMPLETED) {
      return ResponseUtil.error("任务状态错误：未已完成");
    }
    // 修改订单状态
    taskOrder.getOrder().setStatus(OrderStatus.CONFIRMED);

    // 将帐号余额增加
    orderService.modifyUserBalance(taskOrder.getOrder().getAccessorId(), taskOrder.getOrder().getCost());
    return ResponseUtil.success("订单确认完成");
  }

  @PostMapping("/service/confirm/{orderId}")
  public JSONObject confirmServiceOrder(@PathVariable Long orderId) {
    // TODO: token, 获取当前用户id
    // 测试用: userId = 1
    int userId = 1;

    // 已知orderId, 获取order
    OrderDTO serviceOrder = orderService.findById(orderId);
    // 校验用户是否为任务发起者
    if (serviceOrder.getOwner().getId() != userId) {
      return ResponseUtil.error("该用户不是任务发起者");
    }
    // 校验任务状态是否为已完成
    if (serviceOrder.getOrder().getStatus() != OrderStatus.COMPLETED) {
      return ResponseUtil.error("任务状态错误：未已完成");
    }
    // 修改订单状态
    serviceOrder.getOrder().setStatus(OrderStatus.CONFIRMED);

    // 将帐号余额增加
    orderService.modifyUserBalance(serviceOrder.getOrder().getAccessorId(), serviceOrder.getOrder().getCost());
    return ResponseUtil.success("订单确认完成");
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

  @Autowired TaskClient taskClient;

  @GetMapping("/test")
  public Object test(long taskId) {
    return taskClient.getTaskById(taskId);
  }
}

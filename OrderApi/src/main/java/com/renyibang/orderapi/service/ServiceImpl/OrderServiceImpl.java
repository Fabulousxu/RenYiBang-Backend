package com.renyibang.orderapi.service.ServiceImpl;

import com.renyibang.orderapi.client.ServiceClient;
import com.renyibang.orderapi.client.TaskClient;
import com.renyibang.orderapi.client.UserClient;
import com.renyibang.orderapi.dao.OrderDao;
import com.renyibang.orderapi.dto.OrderDTO;
import com.renyibang.orderapi.dto.ServiceDTO;
import com.renyibang.orderapi.dto.TaskDTO;
import com.renyibang.orderapi.dto.UserDTO;
import com.renyibang.orderapi.entity.Order;
import com.renyibang.orderapi.enums.OrderStatus;
import com.renyibang.orderapi.service.OrderService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired private OrderDao orderDao;

  // 使用openfeign替代restTemplate
  @Autowired private UserClient userClient;
  @Autowired private TaskClient taskClient;
  @Autowired private ServiceClient serviceClient;

  @Override
  public List<OrderDTO> findByOwnerIdAndType(long ownerId, Byte type) {
    List<Order> orders = orderDao.findByOwnerIdAndType(ownerId, type);
    List<OrderDTO> orderDTOs = new ArrayList<>();

    // 向User module请求owner信息
    UserDTO owner = userClient.getUserById(ownerId);

    for (Order order : orders) {
      OrderDTO orderDTO = new OrderDTO(order);

      //  向User module请求accessor信息
      UserDTO accessor = userClient.getUserById(order.getAccessorId());

      if (type == 0) {
        // 向Task module请求task信息
        TaskDTO task = taskClient.getTaskById(order.getItemId());
        orderDTO.setTask(task);
      } else {
        // 向Service module请求service信息
        ServiceDTO service = serviceClient.getServiceById(order.getItemId());
        orderDTO.setService(service);
      }

      orderDTO.setOwner(owner);
      orderDTO.setAccessor(accessor);
      orderDTOs.add(orderDTO);
    }

    return orderDTOs;
  }

  @Override
  public List<OrderDTO> findByAccessorIdAndType(long accessorId, Byte type) {
    List<Order> orders = orderDao.findByAccessorIdAndType(accessorId, type);
    List<OrderDTO> orderDTOs = new ArrayList<>();

    // 向User module请求accessor信息
    UserDTO accessor = userClient.getUserById(accessorId);

    for (Order order : orders) {
      OrderDTO orderDTO = new OrderDTO(order);

      //  向User module请求owner信息
      UserDTO owner = userClient.getUserById(order.getOwnerId());

      if (type == 0) {
        // 向Task module请求task信息
        TaskDTO task = taskClient.getTaskById(order.getItemId());
        orderDTO.setTask(task);
      } else {
        // 向Service module请求service信息
        ServiceDTO service = serviceClient.getServiceById(order.getItemId());
        orderDTO.setService(service);
      }

      orderDTO.setOwner(owner);
      orderDTO.setAccessor(accessor);
      orderDTOs.add(orderDTO);
    }

    return orderDTOs;
  }

  @Override
  public List<OrderDTO> findByItemIdAndType(long taskId, Byte type) {
    List<Order> orders = orderDao.findByItemIdAndType(taskId, type);
    List<OrderDTO> orderDTOs = new ArrayList<>();

    this.mapOrdersToOrderDTOs(orders, orderDTOs);

    return orderDTOs;
  }

  @Override
  public List<OrderDTO> findByStatusAndType(OrderStatus status, Byte type) {
    List<Order> orders = orderDao.findByStatusAndType(status, type);
    List<OrderDTO> orderDTOs = new ArrayList<>();

    this.mapOrdersToOrderDTOs(orders, orderDTOs);

    return orderDTOs;
  }

  @Override
  public OrderDTO findById(long orderId) {
    Order order = orderDao.findById(orderId);
    OrderDTO orderDTO = new OrderDTO(order);
    this.mapOrderToOrderDTO(order, orderDTO);
    return orderDTO;
  }

  @Override
  public List<OrderDTO> findAllOrders() {
    List<Order> orders = orderDao.findAll();
    List<OrderDTO> orderDTOs = new ArrayList<>();

    this.mapOrdersToOrderDTOs(orders, orderDTOs);

    return orderDTOs;
  }

  @Override
  public long createOrder(long taskId, long ownerId, long accessorId, long cost, Byte type) {
    // TODO 校验：任务是否存在

    // Create a new Order
    Order order = new Order();
    order.setItemId(taskId);
    order.setOwnerId(ownerId);
    order.setAccessorId(accessorId);
    order.setCost(cost);
    order.setStatus(OrderStatus.UNPAID);

    // Save the Order and return its ID
    return orderDao.save(order).getOrderId();
  }

  @Override
  public boolean setOrderStatusForce(long orderId, OrderStatus status) {
    Order order = orderDao.findById(orderId);
    if (order == null) {
      return false;
    }
    order.setStatus(status);
    orderDao.save(order);
    return true;
  }

  @Override
  public boolean checkOrderExist(long orderId) {
    return orderDao.existsById(orderId);
  }

  @Override
  public boolean checkOrderStatus(long orderId, OrderStatus status) {
    return orderDao.findById(orderId).getStatus() == status;
  }

  @Override
  public void payOrder(Order order) {
    // 获得任务发布者
    long ownerId = order.getOwnerId();
    this.modifyUserBalance(ownerId, -order.getCost());

    // 修改订单状态
    order.setStatus(OrderStatus.IN_PROGRESS);
    orderDao.save(order);
  }

  @Override
  public void completeOrder(Order order) {
    order.setStatus(OrderStatus.COMPLETED);
    orderDao.save(order);
  }

  @Override
  public void confirmOrder(Order order) {
    order.setStatus(OrderStatus.CONFIRMED);
    // 将帐号余额增加
    this.modifyUserBalance(order.getAccessorId(), order.getCost());
    orderDao.save(order);
  }

  @Override
  public void cancelOrder(Order order) {
    order.setStatus(OrderStatus.CANCELLED);
    // 将帐号余额增加
    this.modifyUserBalance(order.getOwnerId(), order.getCost());
    orderDao.save(order);
  }

  @Override
  public void modifyUserBalance(long userId, long amount) {
    // 向User module请求用户信息
    UserDTO user = userClient.getUserById(userId);
    user.setBalance(user.getBalance() + amount);
    userClient.updateUser(user);
  }

  @Override
  public void mapOrderToOrderDTO(Order order, OrderDTO orderDTO) {
    //  向User module请求owner信息
    UserDTO owner = userClient.getUserById(order.getOwnerId());

    //  向User module请求accessor信息
    UserDTO accessor = userClient.getUserById(order.getAccessorId());

    if (order.getType() == 0) {
      // 向Task module请求task信息
      TaskDTO task = taskClient.getTaskById(order.getItemId());
      orderDTO.setTask(task);
    } else {
      // 向Service module请求service信息
      ServiceDTO service = serviceClient.getServiceById(order.getItemId());
      orderDTO.setService(service);
    }

    orderDTO.setOrder(order);
    orderDTO.setOwner(owner);
    orderDTO.setAccessor(accessor);
  }

  @Override
  public void mapOrdersToOrderDTOs(List<Order> orders, List<OrderDTO> orderDTOs) {
    for (Order order : orders) {
      OrderDTO orderDTO = new OrderDTO(order);
      this.mapOrderToOrderDTO(order, orderDTO);
      orderDTOs.add(orderDTO);
    }
  }

  @Override
  public Pair<Boolean, String> markOrderStatus(long orderId, OrderStatus status) {
    Order order = orderDao.findById(orderId);
    if (order == null) {
      return Pair.of(false, "订单不存在");
    }

    OrderStatus currentStatus = order.getStatus();
    if (currentStatus == OrderStatus.UNPAID && status == OrderStatus.IN_PROGRESS) {
      this.payOrder(order);
      return Pair.of(true, "订单支付成功");
    } else if (currentStatus == OrderStatus.IN_PROGRESS && status == OrderStatus.COMPLETED) {
      this.completeOrder(order);
      return Pair.of(true, "订单完成成功");
    } else if (currentStatus == OrderStatus.COMPLETED && status == OrderStatus.CONFIRMED) {
      this.confirmOrder(order);
      return Pair.of(true, "订单确认成功");
    } else if (status == OrderStatus.CANCELLED && currentStatus != OrderStatus.CONFIRMED && currentStatus != OrderStatus.CANCELLED) {
      this.cancelOrder(order);
      return Pair.of(true, "订单取消成功");
    } else {
      return Pair.of(false, "订单状态不合法");
    }
  }
}

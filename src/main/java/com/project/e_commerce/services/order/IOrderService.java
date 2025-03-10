package com.project.e_commerce.services.order;


import com.project.e_commerce.dtos.OrderDTO;

import com.project.e_commerce.models.Order;
import com.project.e_commerce.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO);
    Order getOrderById(long orderId);
    List<Order> getAllOrdersByUserId(long userId);
    Order updateOrder(long orderId,OrderDTO orderDTO);
    void deleteOrder(long orderId);
}

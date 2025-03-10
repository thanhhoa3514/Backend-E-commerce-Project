package com.project.e_commerce.services.order;


import com.project.e_commerce.dtos.OrderDTO;

import com.project.e_commerce.models.Order;
import com.project.e_commerce.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO);
    OrderResponse getOrderById(long orderId);
    List<OrderResponse> getAllOrdersByUserId(long userId);
    OrderResponse updateOrder(long orderId,OrderDTO orderDTO);
    OrderResponse partialUpdateOrder(long orderId, OrderDTO orderDTO);
    void deleteOrder(long orderId);
    void updateOrderStatus(long orderId, String status);

}

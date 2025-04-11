package com.project.e_commerce.services.order.commands;

import com.project.e_commerce.dtos.order.OrderDTO;
import com.project.e_commerce.responses.OrderResponse;

public interface IOrderCommandService {
    OrderResponse createOrder(OrderDTO orderDTO);
    OrderResponse updateOrder(long orderId, OrderDTO orderDTO);
    OrderResponse partialUpdateOrder(long orderId, OrderDTO orderDTO);
    void deleteOrder(long orderId);
    void updateOrderStatus(long orderId, String status);
}

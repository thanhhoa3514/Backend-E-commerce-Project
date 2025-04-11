package com.project.e_commerce.services.order.mappers;

import com.project.e_commerce.dtos.order.OrderDTO;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.responses.OrderResponse;

public interface IOrderMapperService {
    Order mapToOrder(OrderDTO orderDTO);
    OrderResponse mapToOrderResponse(Order order);
    void updateOrderFromDTO(Order order, OrderDTO orderDTO);
    void partialUpdateOrderFromDTO(Order order, OrderDTO orderDTO);
}

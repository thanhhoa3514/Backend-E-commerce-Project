package com.project.e_commerce.services.order.queries;

import com.project.e_commerce.responses.OrderResponse;

import java.util.List;

public interface IOrderQueryService {
    OrderResponse getOrderById(long orderId);
    List<OrderResponse> getAllOrdersByUserId(long userId);

}

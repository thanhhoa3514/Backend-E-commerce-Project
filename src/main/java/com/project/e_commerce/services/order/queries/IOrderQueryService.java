package com.project.e_commerce.services.order.queries;

import com.project.e_commerce.models.Order;
import com.project.e_commerce.responses.OrderResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderQueryService {
    OrderResponse getOrderById(long orderId);
    List<OrderResponse> getAllOrdersByUserId(long userId);
    Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);

}

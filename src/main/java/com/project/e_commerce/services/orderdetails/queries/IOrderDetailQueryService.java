package com.project.e_commerce.services.orderdetails.queries;

import com.project.e_commerce.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailQueryService {
    OrderDetailResponse getOrderDetailById(long orderDetailId);
    List<OrderDetailResponse> getAllOrderDetailsByOrderId(long orderId);
}

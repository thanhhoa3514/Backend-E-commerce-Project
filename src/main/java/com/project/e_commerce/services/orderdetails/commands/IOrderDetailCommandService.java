package com.project.e_commerce.services.orderdetails.commands;

import com.project.e_commerce.dtos.OrderDetailDTO;
import com.project.e_commerce.responses.OrderDetailResponse;

public interface IOrderDetailCommandService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO);
    OrderDetailResponse updateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO);
    OrderDetailResponse partialUpdateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(long orderDetailId);
}

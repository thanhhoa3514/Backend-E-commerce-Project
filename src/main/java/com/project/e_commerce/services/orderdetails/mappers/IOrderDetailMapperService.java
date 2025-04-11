package com.project.e_commerce.services.orderdetails.mappers;

import com.project.e_commerce.dtos.order.OrderDetailDTO;
import com.project.e_commerce.models.OrderDetail;
import com.project.e_commerce.responses.OrderDetailResponse;

public interface IOrderDetailMapperService {
    OrderDetail mapToOrderDetail(OrderDetailDTO orderDetailDTO);
    OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail);
    void updateOrderDetailFromDTO(OrderDetail orderDetail, OrderDetailDTO orderDetailDTO);
    void partialUpdateOrderDetailFromDTO(OrderDetail orderDetail, OrderDetailDTO orderDetailDTO);
}

package com.project.e_commerce.services.orderdetails;

import com.project.e_commerce.dtos.OrderDetailDTO;
import com.project.e_commerce.models.OrderDetail;
import com.project.e_commerce.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO);
    OrderDetailResponse getOrderDetailById(long orderDetailId);
    List<OrderDetailResponse> getAllOrderDetailsByOrderId(long orderId);
//    OrderDetail updateOrderDetail(long orderDetailId, OrderDetail orderDetail);

    OrderDetailResponse partialUpdateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO);
    OrderDetailResponse updateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO);

    void deleteOrderDetail(long orderDetailId);

}

package com.project.e_commerce.services.orderdetails;

import com.project.e_commerce.dtos.OrderDetailDTO;
import com.project.e_commerce.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO);
    OrderDetail getOrderDetailById(long orderDetailId);
    List<OrderDetail> getAllOrderDetailsByOrderId(long orderId);
//    OrderDetail updateOrderDetail(long orderDetailId, OrderDetail orderDetail);

    OrderDetail updateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO);

    void deleteOrderDetail(long orderDetailId);

}

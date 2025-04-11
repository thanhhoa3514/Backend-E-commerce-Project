package com.project.e_commerce.services.orderdetails;

import com.project.e_commerce.dtos.order.OrderDetailDTO;
import com.project.e_commerce.responses.OrderDetailResponse;
import com.project.e_commerce.services.orderdetails.commands.IOrderDetailCommandService;
import com.project.e_commerce.services.orderdetails.queries.IOrderDetailQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final IOrderDetailCommandService orderDetailCommandService;
    private final IOrderDetailQueryService orderDetailQueryService;


    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) {
        return orderDetailCommandService.createOrderDetail(orderDetailDTO);
    }

    @Override
    public OrderDetailResponse getOrderDetailById(long orderDetailId) {
        return orderDetailQueryService.getOrderDetailById(orderDetailId);
    }

    @Override
    public List<OrderDetailResponse> getAllOrderDetailsByOrderId(long orderId) {
        return orderDetailQueryService.getAllOrderDetailsByOrderId(orderId);
    }

    @Override
    public OrderDetailResponse partialUpdateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO) {
        return orderDetailCommandService.partialUpdateOrderDetail(orderDetailId, orderDetailDTO);
    }


    @Override
    public OrderDetailResponse updateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO) {
        return orderDetailCommandService.updateOrderDetail(orderDetailId, orderDetailDTO);
    }

    @Override
    public void deleteOrderDetail(long orderDetailId) {
        orderDetailCommandService.deleteOrderDetail(orderDetailId);
    }


}

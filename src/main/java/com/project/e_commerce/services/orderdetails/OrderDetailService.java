package com.project.e_commerce.services.orderdetails;

import com.project.e_commerce.dtos.OrderDetailDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.OrderDetail;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.repositories.OrderDetailRepository;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.responses.OrderDetailResponse;
import com.project.e_commerce.services.order.commands.IOrderCommandService;
import com.project.e_commerce.services.orderdetails.commands.IOrderDetailCommandService;
import com.project.e_commerce.services.orderdetails.queries.IOrderDetailQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

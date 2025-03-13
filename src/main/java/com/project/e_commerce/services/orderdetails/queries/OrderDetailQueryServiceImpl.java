package com.project.e_commerce.services.orderdetails.queries;

import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.OrderDetail;
import com.project.e_commerce.repositories.OrderDetailRepository;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.responses.OrderDetailResponse;
import com.project.e_commerce.services.orderdetails.mappers.IOrderDetailMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailQueryServiceImpl implements  IOrderDetailQueryService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final IOrderDetailMapperService orderDetailMapperService;

    @Override
    public OrderDetailResponse getOrderDetailById(long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));
        return orderDetailMapperService.mapToOrderDetailResponse(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> getAllOrderDetailsByOrderId(long orderId) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));

        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByOrderId(orderId);
        return orderDetails.stream()
                .map(orderDetailMapperService::mapToOrderDetailResponse)
                .collect(Collectors.toList());
    }
}

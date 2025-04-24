package com.project.e_commerce.services.order.queries;

import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.responses.OrderResponse;
import com.project.e_commerce.services.order.mappers.IOrderMapperService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements  IOrderQueryService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final IOrderMapperService orderMapperService;
    @Override
    public OrderResponse getOrderById(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));
        return orderMapperService.mapToOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User Not Found"));

        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderMapperService::mapToOrderResponse)
                .collect(Collectors.toList());
    }
    @Override
    public Page<Order> getOrdersByKeyword(String keyword, Pageable pageable) {
        return orderRepository.findByKeyword(keyword, pageable);
    }
}

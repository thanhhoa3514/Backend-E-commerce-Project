package com.project.e_commerce.services.order.commands;

import com.project.e_commerce.dtos.OrderDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.OrderStatus;
import com.project.e_commerce.models.User;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.responses.OrderResponse;
import com.project.e_commerce.services.order.mappers.IOrderMapperService;
import com.project.e_commerce.services.order.mappers.OrderMapperServiceImpl;
import com.project.e_commerce.services.order.validation.OrderValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements IOrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderMapperServiceImpl orderMapperService;
    private final UserRepository userRepository;
    private final OrderValidationService orderValidationService;
    private final IOrderMapperService orderMapperService;
    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User Not Found"));

        Order order = orderMapperService.mapToOrder(orderDTO);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setActive(true);

        LocalDateTime shippingDateTime = orderDTO.getShippingDate() != null ?
                orderDTO.getShippingDate().atStartOfDay() : LocalDateTime.now();
        orderValidationService.validateShippingDate(shippingDateTime);
        order.setShippingDate(shippingDateTime);

        Order savedOrder = orderRepository.save(order);
        return orderMapperService.mapToOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse updateOrder(long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));

        if (orderDTO.getUserId() != null) {
            User newUser = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("User Not Found"));
            existingOrder.setUser(newUser);
        }

        orderMapperService.updateOrderFromDTO(existingOrder, orderDTO);

        if (orderDTO.getShippingDate() != null) {
            LocalDateTime shippingDateTime = orderDTO.getShippingDate().atStartOfDay();
            orderValidationService.validateShippingDate(shippingDateTime);
            existingOrder.setShippingDate(shippingDateTime);
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapperService.mapToOrderResponse(updatedOrder);
    }

    @Override
    public OrderResponse partialUpdateOrder(long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));

        orderMapperService.partialUpdateOrderFromDTO(existingOrder, orderDTO);

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapperService.mapToOrderResponse(updatedOrder);
    }

    @Override
    public void deleteOrder(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));

        orderValidationService.validateOrderForDeletion(order);
        order.setActive(false);
        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));

        order.setOrderStatus(OrderStatus.convertStringToEnum(status));
        orderRepository.save(order);
    }
}

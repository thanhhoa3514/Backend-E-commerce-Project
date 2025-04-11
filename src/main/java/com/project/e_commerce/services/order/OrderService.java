package com.project.e_commerce.services.order;

import com.project.e_commerce.dtos.order.OrderDTO;

import com.project.e_commerce.models.user.User;
import com.project.e_commerce.responses.OrderResponse;
import com.project.e_commerce.services.order.commands.IOrderCommandService;
import com.project.e_commerce.services.order.queries.IOrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class OrderService implements  IOrderService{

    private final IOrderCommandService orderCommandService;
    private final IOrderQueryService orderQueryService;

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public OrderResponse createOrder(OrderDTO orderDTO) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (!orderDTO.getUserId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized: Cannot create order for other users");
        }
        return orderCommandService.createOrder(orderDTO);
    }

    @Override
    public OrderResponse getOrderById(long orderId) {
        return orderQueryService.getOrderById(orderId);

    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(long userId) {
        return orderQueryService.getAllOrdersByUserId(userId);
    }

    @Override
    public OrderResponse updateOrder(long orderId, OrderDTO orderDTO) {
        return orderCommandService.updateOrder(orderId, orderDTO);
    }

    @Override
    public OrderResponse partialUpdateOrder(long orderId, OrderDTO orderDTO) {
        return orderCommandService.partialUpdateOrder(orderId, orderDTO);
    }
    @Override
    public void deleteOrder(long orderId) {
        orderCommandService.deleteOrder(orderId);
    }

    @Override
    public void updateOrderStatus(long orderId, String status) {
        orderCommandService.updateOrderStatus(orderId, status);
    }

}

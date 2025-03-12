package com.project.e_commerce.services.order;

import com.project.e_commerce.dtos.OrderDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.OrderStatus;
import com.project.e_commerce.models.User;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.responses.OrderResponse;
import com.project.e_commerce.services.order.commands.IOrderCommandService;
import com.project.e_commerce.services.order.queries.IOrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.e_commerce.models.OrderStatus.convertStringToEnum;


@Service
@RequiredArgsConstructor
public class OrderService implements  IOrderService{

    private final IOrderCommandService orderCommandService;
    private final IOrderQueryService orderQueryService;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
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

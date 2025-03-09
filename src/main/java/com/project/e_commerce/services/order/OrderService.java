package com.project.e_commerce.services.order;

import com.project.e_commerce.dtos.OrderDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.OrderStatus;
import com.project.e_commerce.models.User;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService implements  IOrderService{

    private final OrderRepository orderRepository;
    private final UserRepository  userRepository;
    private final ModelMapper  modelMapper;
    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
        User user=userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(()->new DataNotFoundException("User Not Found with this id "+
                        orderDTO.getUserId()) );

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        Order order = modelMapper.map(orderDTO, Order.class);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);


        LocalDateTime shippingDate = (orderDTO.getShippingDate() != null)? orderDTO.getShippingDate() : LocalDateTime.now();
        if (orderDTO.getShippingDate() != null &&orderDTO.getShippingDate().isBefore(LocalDateTime.now())) {
            throw new DataNotFoundException("Shipping Date must be after the current date");
        }
        order.setActive(true);
        order.setShippingDate(shippingDate);

        Order savedOrder = orderRepository.save(order);
        modelMapper.typeMap(Order.class, OrderResponse.class)
                .addMappings(mapper -> {
                    // No need to skip ID for Order->OrderResponse mapping since we want the ID in the response
                    mapper.map(Order::getCreatedAt, OrderResponse::setCreated_at);
                    mapper.map(Order::getUpdatedAt, OrderResponse::setUpdated_at);
                });

        return modelMapper.map(savedOrder, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrderById(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found with this id " + orderId));

        if (order.getOrderStatus() == null) {
            throw new IllegalStateException("OrderStatus is null for order id " + orderId);
        }

        System.out.println("Order Status in DB: " + order.getOrderStatus());

        // Configure mapping for timestamps
        modelMapper.typeMap(Order.class, OrderResponse.class)
                .addMappings(mapper -> {
                    mapper.map(Order::getCreatedAt, OrderResponse::setCreated_at);
                    mapper.map(Order::getUpdatedAt, OrderResponse::setUpdated_at);
                });

        return modelMapper.map(order, OrderResponse.class);
//        modelMapper.typeMap(Order.class, OrderResponse.class)
//                .addMappings(mapper -> {
//                    mapper.map(Order::getCreatedAt, OrderResponse::setCreated_at);
//                    mapper.map(Order::getUpdatedAt, OrderResponse::setUpdated_at);
//                });
//        OrderStatus status = OrderStatus.fromString("Pending");
//        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User Not Found with this id " + userId));

        List<Order> orders = orderRepository.findByUserId(userId);
        modelMapper.typeMap(Order.class, OrderResponse.class)
                .addMappings(mapper -> {
                    mapper.map(Order::getCreatedAt, OrderResponse::setCreated_at);
                    mapper.map(Order::getUpdatedAt, OrderResponse::setUpdated_at);
                });
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrder(long orderId, OrderDTO orderDTO) {
        return null;
    }

    @Override
    public void deleteOrder(long orderId) {

    }

}

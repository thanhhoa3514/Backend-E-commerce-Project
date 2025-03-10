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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.e_commerce.models.OrderStatus.convertStringToEnum;


@Service
@RequiredArgsConstructor
public class OrderService implements  IOrderService{

    private final OrderRepository orderRepository;
    private final UserRepository  userRepository;
    private final ModelMapper  modelMapper;
    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User Not Found with this id " +
                        orderDTO.getUserId()));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        Order order = modelMapper.map(orderDTO, Order.class);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        // Xử lý shipping date
        LocalDateTime shippingDateTime;
        if (orderDTO.getShippingDate() != null) {
            shippingDateTime = orderDTO.getShippingDate().atStartOfDay(); // Chuyển LocalDate sang LocalDateTime
            if (shippingDateTime.isBefore(LocalDateTime.now())) {
                throw new DataNotFoundException("Shipping Date must be after the current date");
            }
        } else {
            shippingDateTime = LocalDateTime.now();
        }
        
        order.setActive(true);
        order.setShippingDate(shippingDateTime);

        Order savedOrder = orderRepository.save(order);
        modelMapper.typeMap(Order.class, OrderResponse.class)
            .addMappings(mapper -> {
                mapper.map(src -> src.getUser().getId(), OrderResponse::setUserId);
                mapper.map(src -> src.getUser().getFullName(), OrderResponse::setUserName);
                mapper.map(Order::getCreatedAt, OrderResponse::setCreatedAt);
                mapper.map(Order::getUpdatedAt, OrderResponse::setUpdatedAt);
            });

        return modelMapper.map(savedOrder, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrderById(long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new DataNotFoundException("Order Not Found with this id " + orderId));
    
        modelMapper.typeMap(Order.class, OrderResponse.class)
            .addMappings(mapper -> {
                mapper.map(src -> src.getUser().getId(), OrderResponse::setUserId);
                mapper.map(src -> src.getUser().getFullName(), OrderResponse::setUserName);
                mapper.map(Order::getCreatedAt, OrderResponse::setCreatedAt);
            });

    return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new DataNotFoundException("User Not Found with id: " + userId));

        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
            .map(order -> OrderResponse.builder()
                    .id(order.getId())
                    .userId(order.getUser().getId())
                    .userName(order.getUser().getFullName())
                    .fullName(order.getFullname())
                    .email(order.getEmail())
                    .phoneNumber(order.getPhoneNumber())
                    .address(order.getAddress())
                    .notes(order.getNotes())
                    .orderDate(order.getOrderDate())
                    .shippingMethod(order.getShippingMethod())
                    .shippingAddress(order.getShippingAddress())
                    .trackingNumber(order.getTrackingNumber())
                    .shippingDate(order.getShippingDate())
                    .paymentMethod(order.getPaymentMethod())
                    .totalPrice(order.getTotalPrice())
                    .orderStatus(order.getOrderStatus())
                    .createdAt(order.getCreatedAt())
                    .updatedAt(order.getUpdatedAt()) 
                    .build())
            .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrder(long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found with id: " + orderId));

        // Cập nhật tất cả các trường, nếu null thì set về null
        if (orderDTO.getUserId() != null) {
            User newUser = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("User Not Found with id: " + orderDTO.getUserId()));
            existingOrder.setUser(newUser);
        }
        existingOrder.setFullname(orderDTO.getFullName());
        existingOrder.setEmail(orderDTO.getEmail());
        existingOrder.setPhoneNumber(orderDTO.getPhoneNumber());
        existingOrder.setAddress(orderDTO.getAddress());
        existingOrder.setNotes(orderDTO.getNotes());
        existingOrder.setShippingMethod(orderDTO.getShippingMethod());
        existingOrder.setShippingAddress(orderDTO.getShippingAddress());
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        existingOrder.setTotalPrice(orderDTO.getTotalPrice());
        if (orderDTO.getShippingDate() != null) {
            LocalDateTime shippingDateTime = orderDTO.getShippingDate().atStartOfDay();
            if (shippingDateTime.isBefore(LocalDateTime.now())) {
                throw new DataNotFoundException("Shipping Date must be after the current date");
            }
            existingOrder.setShippingDate(shippingDateTime);
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return modelMapper.map(updatedOrder, OrderResponse.class);
    }

    @Override
    public OrderResponse partialUpdateOrder(long orderId, OrderDTO orderDTO) {
        // Phương thức mới cho PATCH - cập nhật từng phần
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found with id: " + orderId));

        // Chỉ cập nhật các trường không null
        if (orderDTO.getUserId() != null) {
            User newUser = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("User Not Found with id: " + orderDTO.getUserId()));
            existingOrder.setUser(newUser);
        }
        if (orderDTO.getFullName() != null) existingOrder.setFullname(orderDTO.getFullName());
        if (orderDTO.getEmail() != null) existingOrder.setEmail(orderDTO.getEmail());
        if (orderDTO.getPhoneNumber() != null) existingOrder.setPhoneNumber(orderDTO.getPhoneNumber());
        if (orderDTO.getAddress() != null) existingOrder.setAddress(orderDTO.getAddress());
        if (orderDTO.getNotes() != null) existingOrder.setNotes(orderDTO.getNotes());
        if (orderDTO.getShippingMethod() != null) existingOrder.setShippingMethod(orderDTO.getShippingMethod());
        if (orderDTO.getShippingAddress() != null) existingOrder.setShippingAddress(orderDTO.getShippingAddress());
        if (orderDTO.getPaymentMethod() != null) existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        if (orderDTO.getTotalPrice() != null) existingOrder.setTotalPrice(orderDTO.getTotalPrice());
        if (orderDTO.getShippingDate() != null) {
            LocalDateTime shippingDateTime = orderDTO.getShippingDate().atStartOfDay();
            if (shippingDateTime.isBefore(LocalDateTime.now())) {
                throw new DataNotFoundException("Shipping Date must be after the current date");
            }
            existingOrder.setShippingDate(shippingDateTime);
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return modelMapper.map(updatedOrder, OrderResponse.class);
    }
    @Override
    public void deleteOrder(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found with id: " + orderId));

        // Có thể thêm logic kiểm tra trước khi xóa
        // Ví dụ: không cho xóa đơn hàng đã giao thành công
        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new DataNotFoundException("Cannot delete completed order");
        }

        // Soft delete: set active = false thay vì xóa khỏi database
        order.setActive(false);
        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found with id: " + orderId));

        order.setOrderStatus(OrderStatus.convertStringToEnum(status));
        orderRepository.save(order);

    }

}

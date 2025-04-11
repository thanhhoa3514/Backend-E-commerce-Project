package com.project.e_commerce.services.order.commands;

import com.project.e_commerce.dtos.order.OrderDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.enums.OrderStatus;
import com.project.e_commerce.enums.ShippingMethod;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.responses.OrderResponse;
import com.project.e_commerce.services.order.DeliveryTimeCalculator;
import com.project.e_commerce.services.order.mappers.IOrderMapperService;
import com.project.e_commerce.services.order.validation.OrderValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements IOrderCommandService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderValidationService orderValidationService;
    private final IOrderMapperService orderMapperService;
    private final DeliveryTimeCalculator deliveryTimeCalculator;


    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User Not Found"));

        Order order = orderMapperService.mapToOrder(orderDTO);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setActive(true);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Calculate delivery time based on shipping method
        updateDeliveryTimeEstimates(order, orderDTO.getShippingMethod());

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

        // Recalculate delivery time if shipping method changes
        if (orderDTO.getShippingMethod() != null && 
            !orderDTO.getShippingMethod().equals(existingOrder.getShippingMethod())) {
            updateDeliveryTimeEstimates(existingOrder, orderDTO.getShippingMethod());
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapperService.mapToOrderResponse(updatedOrder);
    }

    private void updateDeliveryTimeEstimates(Order order, ShippingMethod shippingMethod) {
        DeliveryTimeCalculator.DeliveryTimeRange deliveryTime = 
            deliveryTimeCalculator.calculateDeliveryTime(shippingMethod);
        order.setEstimatedDeliveryFrom(deliveryTime.getFromDate());
        order.setEstimatedDeliveryTo(deliveryTime.getToDate());
    }

    @Override
    public OrderResponse partialUpdateOrder(long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));

        // Validate order status before allowing updates
        if (existingOrder.getOrderStatus() == OrderStatus.COMPLETED || 
            existingOrder.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new DataNotFoundException("Cannot update completed or cancelled order");
        }

        orderMapperService.partialUpdateOrderFromDTO(existingOrder, orderDTO);

        // Recalculate delivery time if shipping method changes
        if (orderDTO.getShippingMethod() != null && 
            !orderDTO.getShippingMethod().equals(existingOrder.getShippingMethod())) {
            updateDeliveryTimeEstimates(existingOrder, orderDTO.getShippingMethod());
        }

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

        OrderStatus newStatus = OrderStatus.convertStringToEnum(status);
        orderValidationService.validateStatusTransition(order, newStatus);
        
        // Khi chuyển sang trạng thái SHIPPING
        if (newStatus == OrderStatus.SHIPPING) {
            // Set shipping date là thời điểm hiện tại
            order.setShippingDate(LocalDateTime.now());
            // Tạo tracking number
            order.setTrackingNumber(generateTrackingNumber(order));
        }
        
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }

    private String generateTrackingNumber(Order order) {
        // Format: ORD-[OrderId]-[Timestamp]
        return String.format("ORD-%d-%d", 
            order.getId(), 
            System.currentTimeMillis());
    }
}

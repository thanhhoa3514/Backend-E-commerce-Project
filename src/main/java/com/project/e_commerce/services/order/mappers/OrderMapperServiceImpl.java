package com.project.e_commerce.services.order.mappers;


import com.project.e_commerce.dtos.order.OrderDTO;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMapperServiceImpl implements IOrderMapperService {
    private final ModelMapper modelMapper;

    public Order mapToOrder(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    public OrderResponse mapToOrderResponse(Order order) {
        // Create a new OrderResponse and set fields manually
        OrderResponse response = new OrderResponse();
        
        // Basic fields
        response.setId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setUserName(order.getUser().getFullName());
        response.setFullName(order.getFullname());
        response.setEmail(order.getEmail());
        response.setPhoneNumber(order.getPhoneNumber());
        response.setAddress(order.getAddress());
        response.setNotes(order.getNotes());
        response.setOrderDate(order.getOrderDate());
        response.setShippingMethod(order.getShippingMethod().name());
        response.setShippingAddress(order.getShippingAddress());
        response.setTrackingNumber(order.getTrackingNumber());
        response.setShippingDate(order.getShippingDate());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setTotalPrice(order.getTotalPrice());
        response.setOrderStatus(order.getOrderStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        
        // Handle estimated delivery time separately
        if (order.getEstimatedDeliveryFrom() != null && order.getEstimatedDeliveryTo() != null) {
            response.setEstimatedDeliveryTime(String.format("Dự kiến giao hàng từ %s đến %s", 
                order.getEstimatedDeliveryFrom().toString(), 
                order.getEstimatedDeliveryTo().toString()));
        }
        
        return response;
    }

    @Override
    public void updateOrderFromDTO(Order order, OrderDTO orderDTO) {
        order.setFullname(orderDTO.getFullName());
        order.setEmail(orderDTO.getEmail());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setAddress(orderDTO.getAddress());
        order.setNotes(orderDTO.getNotes());
        order.setShippingMethod(orderDTO.getShippingMethod());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setTotalPrice(orderDTO.getTotalPrice());
    }

    @Override
    public void partialUpdateOrderFromDTO(Order order, OrderDTO orderDTO) {
        if (orderDTO.getFullName() != null) order.setFullname(orderDTO.getFullName());
        if (orderDTO.getEmail() != null) order.setEmail(orderDTO.getEmail());
        if (orderDTO.getPhoneNumber() != null) order.setPhoneNumber(orderDTO.getPhoneNumber());
        if (orderDTO.getAddress() != null) order.setAddress(orderDTO.getAddress());
        if (orderDTO.getNotes() != null) order.setNotes(orderDTO.getNotes());
        if (orderDTO.getShippingMethod() != null) order.setShippingMethod(orderDTO.getShippingMethod());
        if (orderDTO.getShippingAddress() != null) order.setShippingAddress(orderDTO.getShippingAddress());
        if (orderDTO.getPaymentMethod() != null) order.setPaymentMethod(orderDTO.getPaymentMethod());
        if (orderDTO.getTotalPrice() != null) order.setTotalPrice(orderDTO.getTotalPrice());
    }
}

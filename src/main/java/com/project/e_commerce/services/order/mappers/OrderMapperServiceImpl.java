package com.project.e_commerce.services.order.mappers;


import com.project.e_commerce.dtos.OrderDTO;
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
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        return modelMapper.map(orderDTO, Order.class);
    }

    public OrderResponse mapToOrderResponse(Order order) {
        modelMapper.typeMap(Order.class, OrderResponse.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUser().getId(), OrderResponse::setUserId);
                    mapper.map(src -> src.getUser().getFullName(), OrderResponse::setUserName);
                    mapper.map(Order::getCreatedAt, OrderResponse::setCreatedAt);
                    mapper.map(Order::getUpdatedAt, OrderResponse::setUpdatedAt);
                });
        return modelMapper.map(order, OrderResponse.class);
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

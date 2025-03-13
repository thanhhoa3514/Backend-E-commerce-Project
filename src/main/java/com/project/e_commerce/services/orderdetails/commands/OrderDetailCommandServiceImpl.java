package com.project.e_commerce.services.orderdetails.commands;


import com.project.e_commerce.dtos.OrderDetailDTO;
import com.project.e_commerce.repositories.OrderDetailRepository;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.responses.OrderDetailResponse;
import com.project.e_commerce.services.orderdetails.mappers.IOrderDetailMapperService;
import com.project.e_commerce.services.orderdetails.validation.OrderDetailValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailCommandServiceImpl implements IOrderDetailCommandService {


    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final IOrderDetailMapperService orderDetailMapperService;
    private final OrderDetailValidationService orderDetailValidationService;

    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) {
        return null;
    }

    @Override
    public OrderDetailResponse updateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO) {
        return null;
    }

    @Override
    public OrderDetailResponse partialUpdateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO) {
        return null;
    }

    @Override
    public void deleteOrderDetail(long orderDetailId) {

    }
}

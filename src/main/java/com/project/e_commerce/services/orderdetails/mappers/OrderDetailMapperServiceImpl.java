package com.project.e_commerce.services.orderdetails.mappers;

import com.project.e_commerce.dtos.OrderDetailDTO;
import com.project.e_commerce.models.OrderDetail;
import com.project.e_commerce.responses.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailMapperServiceImpl implements  IOrderDetailMapperService {
    private final ModelMapper modelMapper;

    /***
     * Mapping orderdetail dto to order detail
     *
     *
     * */
    @Override
    public OrderDetail mapToOrderDetail(OrderDetailDTO orderDetailDTO) {
        modelMapper.typeMap(OrderDetailDTO.class, OrderDetail.class)
                .addMappings(mapper -> mapper.skip(OrderDetail::setId));
        return modelMapper.map(orderDetailDTO, OrderDetail.class);
    }



    @Override
    public OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .productName(orderDetail.getProduct().getName())
                .quantity(orderDetail.getQuantity())
                .totalMoney(orderDetail.getTotalMoney())
                .color(orderDetail.getColor())
                .createdAt(orderDetail.getCreatedAt())
                .build();
    }

    @Override
    public void updateOrderDetailFromDTO(OrderDetail orderDetail, OrderDetailDTO orderDetailDTO) {
        orderDetail.setQuantity(orderDetailDTO.getQuantity());
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
    }

    @Override
    public void partialUpdateOrderDetailFromDTO(OrderDetail orderDetail, OrderDetailDTO orderDetailDTO) {
        if (orderDetailDTO.getQuantity() > 0) orderDetail.setQuantity(orderDetailDTO.getQuantity());
        if (orderDetailDTO.getColor() != null) orderDetail.setColor(orderDetailDTO.getColor());
        if (orderDetailDTO.getTotalMoney() > 0) orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
    }
}

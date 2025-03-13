package com.project.e_commerce.services.orderdetails;

import com.project.e_commerce.dtos.OrderDetailDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.OrderDetail;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.repositories.OrderDetailRepository;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.responses.OrderDetailResponse;
import com.project.e_commerce.services.order.commands.IOrderCommandService;
import com.project.e_commerce.services.orderdetails.commands.IOrderDetailCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final IOrderCommandService orderCommandService;
    private final IOrderDetailCommandService iOrderDetailCommandService;


    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) {
        return
    }

    @Override
    public OrderDetailResponse getOrderDetailById(long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
            .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));
        return convertToResponse(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> getAllOrderDetailsByOrderId(long orderId) {
        // Kiểm tra order có tồn tại không
        orderRepository.findById(orderId)
            .orElseThrow(() -> new DataNotFoundException("Order Not Found"));
            
        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByOrderId(orderId);
        return orderDetails.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

//    @Override
//    public OrderDetail updateOrderDetail(long orderDetailId, OrderDetail orderDetail) {
//        return null;
//    }

    @Override
    public OrderDetailResponse updateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));

        if (orderDetailDTO.getOrderId() != null &&
                !orderDetailDTO.getOrderId().equals(existingOrderDetail.getOrder().getId())) {
            Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                    .orElseThrow(() -> new DataNotFoundException("Order Not Found"));
            existingOrderDetail.setOrder(order);
        }


        if (orderDetailDTO.getProductId() != null &&
                !orderDetailDTO.getProductId().equals(existingOrderDetail.getProduct().getId())) {
            Product product = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Product Not Found"));
            existingOrderDetail.setProduct(product);
        }

        // Cập nhật các trường có thể thay đổi
        if (orderDetailDTO.getQuantity() > 0) {
            existingOrderDetail.setQuantity(orderDetailDTO.getQuantity());
        }
        if (orderDetailDTO.getColor() != null) {
            existingOrderDetail.setColor(orderDetailDTO.getColor());
        }
        if (orderDetailDTO.getTotalMoney() > 0) {
            existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        }

        OrderDetail updatedOrderDetail = orderDetailRepository.save(existingOrderDetail);
        return convertToResponse(updatedOrderDetail);
    }

    @Override
    public void deleteOrderDetail(long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));
        orderDetailRepository.delete(orderDetail);
    }

    private OrderDetailResponse convertToResponse(OrderDetail orderDetail) {
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
}

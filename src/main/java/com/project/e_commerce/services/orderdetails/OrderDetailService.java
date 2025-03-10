package com.project.e_commerce.services.orderdetails;

import com.project.e_commerce.dtos.OrderDetailDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.OrderDetail;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.repositories.OrderDetailRepository;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));

        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product Not Found"));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(orderDetailDTO.getQuantity())
                .color(orderDetailDTO.getColor())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .build();
        
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetailById(long orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));
    }

    @Override
    public List<OrderDetail> getAllOrderDetailsByOrderId(long orderId) {
        // Kiểm tra order có tồn tại không
        orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));
                
        return orderDetailRepository.findOrderDetailByOrderId(orderId);
    }

//    @Override
//    public OrderDetail updateOrderDetail(long orderDetailId, OrderDetail orderDetail) {
//        return null;
//    }

    @Override
    public OrderDetail updateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));

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

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));
        orderDetailRepository.delete(orderDetail);
    }
}

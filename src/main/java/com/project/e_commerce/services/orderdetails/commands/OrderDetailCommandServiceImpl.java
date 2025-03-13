package com.project.e_commerce.services.orderdetails.commands;


import com.project.e_commerce.dtos.OrderDetailDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.OrderDetail;
import com.project.e_commerce.models.Product;
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
        // Validate inputs
        orderDetailValidationService.validateQuantity(orderDetailDTO.getQuantity());
        orderDetailValidationService.validateTotalMoney(orderDetailDTO.getTotalMoney());

        // Get related entities
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order Not Found"));

        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product Not Found"));

        // Create new order detail
        OrderDetail orderDetail = orderDetailMapperService.mapToOrderDetail(orderDetailDTO);
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        return orderDetailMapperService.mapToOrderDetailResponse(savedOrderDetail);
    }

    @Override
    public OrderDetailResponse updateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO) {
        // Validate inputs
        orderDetailValidationService.validateQuantity(orderDetailDTO.getQuantity());
        orderDetailValidationService.validateTotalMoney(orderDetailDTO.getTotalMoney());

        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));

        // Update product if changed
        if (orderDetailDTO.getProductId() != null &&
                !orderDetailDTO.getProductId().equals(existingOrderDetail.getProduct().getId())) {
            Product newProduct = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Product Not Found"));
            existingOrderDetail.setProduct(newProduct);
        }

        orderDetailMapperService.updateOrderDetailFromDTO(existingOrderDetail, orderDetailDTO);

        OrderDetail updatedOrderDetail = orderDetailRepository.save(existingOrderDetail);
        return orderDetailMapperService.mapToOrderDetailResponse(updatedOrderDetail);
    }

    @Override
    public OrderDetailResponse partialUpdateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));

        // Validate if quantity is being updated
        if (orderDetailDTO.getQuantity() > 0) {
            orderDetailValidationService.validateQuantity(orderDetailDTO.getQuantity());
        }

        // Validate if total money is being updated
        if (orderDetailDTO.getTotalMoney() > 0) {
            orderDetailValidationService.validateTotalMoney(orderDetailDTO.getTotalMoney());
        }

        // Update product if provided
        if (orderDetailDTO.getProductId() != null &&
                !orderDetailDTO.getProductId().equals(existingOrderDetail.getProduct().getId())) {
            Product newProduct = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Product Not Found"));
            existingOrderDetail.setProduct(newProduct);
        }

        orderDetailMapperService.partialUpdateOrderDetailFromDTO(existingOrderDetail, orderDetailDTO);

        OrderDetail updatedOrderDetail = orderDetailRepository.save(existingOrderDetail);
        return orderDetailMapperService.mapToOrderDetailResponse(updatedOrderDetail);
    }

    @Override
    public void deleteOrderDetail(long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Order Detail Not Found"));

        orderDetailValidationService.validateForDeletion(orderDetail);
        orderDetailRepository.delete(orderDetail);
    }
}

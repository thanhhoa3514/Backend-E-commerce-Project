package com.project.e_commerce.services.order;

import com.project.e_commerce.dtos.CategoryDTO;
import com.project.e_commerce.dtos.OrderDTO;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createCategory(OrderDTO orderDTO);
    OrderResponse getCategoryById(long orderId);
    List<OrderResponse> getAllCategories();
    OrderResponse updateCategory(long orderId,OrderDTO orderDTO);
    void deleteCategory(long orderId);
}

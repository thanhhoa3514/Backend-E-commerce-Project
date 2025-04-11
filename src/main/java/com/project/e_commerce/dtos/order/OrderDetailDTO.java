package com.project.e_commerce.dtos.order;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1,message = "Order's id must be not empty")
    private Long orderId;


    @JsonProperty("product_id")
    @Min(value = 1,message = "Product's id must be not empty")
    private Long productId;

    @Min(value = 0,message = "Price must be greater than 0")
    private  double price;

    @Min(value = 1,message = "Quantity must be have amount")
    private int quantity;

    @JsonProperty("total_money")
    @Min(value = 0,message = "Price must be greater than zero")
    private double totalMoney;

    private String color;

}

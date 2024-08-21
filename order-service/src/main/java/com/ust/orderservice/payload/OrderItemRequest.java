package com.ust.orderservice.payload;

public record OrderItemRequest(
        String skuCode,
        Integer quantity
) {
}

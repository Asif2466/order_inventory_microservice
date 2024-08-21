package com.ust.orderservice.payload;

import java.util.List;

public record OrderRequest(
        List<OrderItemRequest> orderItems
) {
}

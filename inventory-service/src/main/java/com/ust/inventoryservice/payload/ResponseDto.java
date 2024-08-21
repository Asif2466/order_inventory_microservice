package com.ust.inventoryservice.payload;

import org.springframework.http.HttpStatus;

public record ResponseDto(
        String skuCode,
        boolean available,
        HttpStatus status
) {
}

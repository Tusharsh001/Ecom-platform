package com.Ecom.platform.model.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        String orderId,
        String CustomerName,
        String email,
        String status,
        LocalDate orderDate,
        List<OrderItemResponse> items
) {
}

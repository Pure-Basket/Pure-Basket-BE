package com.example.purebasketbe.domain.product.dto;

import com.example.purebasketbe.domain.product.entity.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

/**
 * DTO for {@link com.example.purebasketbe.domain.product.entity.Product}
 */
public record ProductDto(@NotBlank String name, @PositiveOrZero int price, @PositiveOrZero int stock, String info,
                         String category, Event event, double discountRate) implements Serializable {
}
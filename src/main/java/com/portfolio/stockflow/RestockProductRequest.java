package com.portfolio.stockflow;

import jakarta.validation.constraints.Min;

public record RestockProductRequest(
        @Min(1) int quantity
) {
}

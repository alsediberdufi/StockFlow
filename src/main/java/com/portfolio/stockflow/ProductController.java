package com.portfolio.stockflow;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService products;

    public ProductController(ProductService products) {
        this.products = products;
    }

    @PostMapping
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        return products.create(request);
    }

    @PostMapping("/{id}/restock")
    public ProductResponse restock(
            @PathVariable Long id,
            @Valid @RequestBody RestockProductRequest request
    ) {
        return products.restock(id, request);
    }

    @GetMapping
    public List<ProductResponse> findAll(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "false") boolean lowStockOnly,
            @RequestParam(defaultValue = "4") int lowStockLimit
    ) {
        return products.findAll(category, lowStockOnly, lowStockLimit);
    }

    @GetMapping("/categories")
    public List<String> findCategories() {
        return products.findCategories();
    }

    @GetMapping("/low-stock")
    public List<ProductResponse> findLowStock(@RequestParam(defaultValue = "4") int limit) {
        return products.findLowStockProducts(limit);
    }
}

package com.portfolio.stockflow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository products;

    public ProductService(ProductRepository products) {
        this.products = products;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Product product = new Product(
                request.name(),
                request.category(),
                request.price(),
                request.stock()
        );

        return ProductResponse.from(products.save(product));
    }

    public List<ProductResponse> findAll(String category, boolean lowStockOnly, int lowStockLimit) {
        List<Product> result;

        if (hasText(category) && lowStockOnly) {
            result = products.findByCategoryIgnoreCaseAndStockLessThanEqual(category, lowStockLimit);
        } else if (hasText(category)) {
            result = products.findByCategoryIgnoreCase(category);
        } else if (lowStockOnly) {
            result = products.findByStockLessThanEqual(lowStockLimit);
        } else {
            result = products.findAll();
        }

        return result.stream()
                .sorted(Comparator.comparing(Product::getName))
                .map(ProductResponse::from)
                .toList();
    }

    public List<String> findCategories() {
        return products.findAll()
                .stream()
                .map(Product::getCategory)
                .filter(this::hasText)
                .distinct()
                .sorted()
                .toList();
    }

    public Product findById(Long id) {
        return products.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    public List<ProductResponse> findLowStockProducts(int limit) {
        return products.findByStockLessThanEqual(limit)
                .stream()
                .sorted(Comparator.comparing(Product::getStock).thenComparing(Product::getName))
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional
    public ProductResponse restock(Long id, RestockProductRequest request) {
        Product product = findById(id);
        product.restock(request.quantity());
        return ProductResponse.from(product);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}

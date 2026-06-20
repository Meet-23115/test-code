package com.example.mapper;

import com.example.dto.response.ProductResponse;
import com.example.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        BigDecimal discountedPrice = null;
        if (product.getDiscountPercentage() != null
                && product.getDiscountPercentage().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = product.getPrice()
                    .multiply(product.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            discountedPrice = product.getPrice().subtract(discount);
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .sku(product.getSku())
                .active(product.isActive())
                .featured(product.isFeatured())
                .discountPercentage(product.getDiscountPercentage())
                .discountedPrice(discountedPrice)
                .imageUrl(product.getImageUrl())
                .additionalImages(product.getAdditionalImages())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}

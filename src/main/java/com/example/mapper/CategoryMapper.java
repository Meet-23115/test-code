package com.example.mapper;

import com.example.dto.response.CategoryResponse;
import com.example.entity.Category;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        CategoryResponse.CategoryResponseBuilder builder = CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .active(category.isActive())
                .sortOrder(category.getSortOrder())
                .productCount(category.getProducts() != null ? (long) category.getProducts().size() : 0)
                .createdAt(category.getCreatedAt());

        if (category.getParentCategory() != null) {
            builder.parentCategoryId(category.getParentCategory().getId())
                   .parentCategoryName(category.getParentCategory().getName());
        }

        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            builder.subCategories(category.getSubCategories().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toSet()));
        }

        return builder.build();
    }
}

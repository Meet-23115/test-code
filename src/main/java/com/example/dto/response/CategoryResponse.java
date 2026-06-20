package com.example.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private boolean active;
    private int sortOrder;
    private Long parentCategoryId;
    private String parentCategoryName;
    private Set<CategoryResponse> subCategories;
    private long productCount;
    private LocalDateTime createdAt;
}

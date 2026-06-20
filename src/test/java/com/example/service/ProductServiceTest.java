package com.example.service;

import com.example.dto.request.ProductRequest;
import com.example.dto.response.ProductResponse;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.mapper.ProductMapper;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    private ProductMapper productMapper;
    private ProductService productService;
    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
        productService = new ProductService(productRepository, categoryRepository, productMapper);
        category = Category.builder().id(1L).name("Test Category").build();
        product = Product.builder()
                .id(1L).name("Test Product").price(new BigDecimal("99.99"))
                .stockQuantity(10).sku("TST-001").active(true)
                .category(category).discountPercentage(new BigDecimal("10"))
                .build();
    }

    @Test
    void getProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductResponse response = productService.getProductById(1L);
        assertEquals("Test Product", response.getName());
        assertEquals(new BigDecimal("99.99"), response.getPrice());
        assertNotNull(response.getDiscountedPrice());
    }

    @Test
    void getActiveProducts() {
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findByActiveTrue(any(Pageable.class))).thenReturn(page);
        Page<ProductResponse> result = productService.getActiveProducts(0, 10, "name", "asc");
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void createProduct() {
        ProductRequest request = new ProductRequest("New Product", "Desc",
                new BigDecimal("49.99"), 5, "NEW-001", false, null, 1L, null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductResponse response = productService.createProduct(request);
        assertNotNull(response);
    }

    @Test
    void searchProducts() {
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.searchByKeyword(anyString(), any(Pageable.class))).thenReturn(page);
        Page<ProductResponse> result = productService.searchProducts("test", 0, 10);
        assertEquals(1, result.getTotalElements());
    }
}

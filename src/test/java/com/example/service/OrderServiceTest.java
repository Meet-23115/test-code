package com.example.service;

import com.example.dto.request.OrderRequest;
import com.example.dto.response.OrderResponse;
import com.example.entity.*;
import com.example.enums.OrderStatus;
import com.example.event.OrderCreatedEvent;
import com.example.exception.BadRequestException;
import com.example.mapper.OrderMapper;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductService productService;
    @Mock private ApplicationEventPublisher eventPublisher;
    private OrderMapper orderMapper;
    private OrderService orderService;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapper();
        orderService = new OrderService(orderRepository, productRepository, userRepository,
                orderMapper, productService, eventPublisher);
        user = User.builder().id(1L).fullName("Test User").username("testuser").build();
        product = Product.builder().id(1L).name("Test Product")
                .price(new BigDecimal("50.00")).stockQuantity(10).active(true).build();
    }

    @Test
    void createOrder() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(1L);
            return o;
        });

        OrderRequest request = new OrderRequest(
                List.of(new OrderRequest.OrderItemRequest(1L, 2)),
                "123 Test St", "CREDIT_CARD", "Please deliver fast");

        OrderResponse response = orderService.createOrder(1L, request);
        assertNotNull(response);
        assertEquals(new BigDecimal("100.00"), response.getSubtotal());
        assertTrue(response.getTotalAmount().compareTo(BigDecimal.ZERO) > 0);
        verify(eventPublisher).publishEvent(any(OrderCreatedEvent.class));
    }

    @Test
    void createOrderInsufficientStock() {
        product.setStockQuantity(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OrderRequest request = new OrderRequest(
                List.of(new OrderRequest.OrderItemRequest(1L, 5)),
                "123 Test St", "CREDIT_CARD", null);

        assertThrows(BadRequestException.class, () -> orderService.createOrder(1L, request));
    }
}

package com.example.config;

import com.example.entity.Order;
import com.example.enums.OrderStatus;
import com.example.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private static final Logger log = LoggerFactory.getLogger(SchedulingConfig.class);
    private final OrderRepository orderRepository;

    public SchedulingConfig(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(cron = "0 0 */6 * * *")
    public void cancelStalePendingOrders() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(48);
        List<Order> staleOrders = orderRepository.findByStatusAndCreatedAtBefore(OrderStatus.PENDING, cutoff);
        for (Order order : staleOrders) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            log.info("Auto-cancelled stale order: {}", order.getOrderNumber());
        }
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void logDailySummary() {
        long pendingCount = orderRepository.countByStatus(OrderStatus.PENDING);
        long shippedCount = orderRepository.countByStatus(OrderStatus.SHIPPED);
        log.info("Daily summary - Pending orders: {}, Shipped orders: {}", pendingCount, shippedCount);
    }
}

package com.example.event;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    private final EmailService emailService;
    private final UserRepository userRepository;

    public OrderEventListener(EmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Async
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Order created event received: orderId={}, orderNumber={}, total={}",
                event.getOrderId(), event.getOrderNumber(), event.getTotalAmount());

        User user = userRepository.findById(event.getUserId()).orElse(null);
        if (user != null) {
            emailService.sendOrderConfirmation(user.getEmail(), event.getOrderNumber());
        }
    }
}

package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to: {}", to);
        } catch (Exception ex) {
            log.error("Failed to send email to {}: {}", to, ex.getMessage());
        }
    }

    @Async
    public void sendWelcomeEmail(String to, String name) {
        sendEmail(to, "Welcome to Enterprise App",
                "Hello " + name + ",\n\nWelcome to Enterprise App! " +
                "Your account has been created successfully.\n\nBest regards,\nEnterprise Team");
    }

    @Async
    public void sendOrderConfirmation(String to, String orderNumber) {
        sendEmail(to, "Order Confirmation - " + orderNumber,
                "Your order " + orderNumber + " has been placed successfully.\n\n" +
                "Thank you for shopping with us!\n\nEnterprise Team");
    }
}

package com.example.config;

import com.example.entity.*;
import com.example.enums.OrderStatus;
import com.example.enums.RoleName;
import com.example.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository,
                      CategoryRepository categoryRepository, ProductRepository productRepository,
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            log.info("Database already seeded, skipping");
            return;
        }

        log.info("Seeding database...");

        Role userRole = roleRepository.save(new Role(null, RoleName.ROLE_USER));
        Role modRole = roleRepository.save(new Role(null, RoleName.ROLE_MODERATOR));
        Role adminRole = roleRepository.save(new Role(null, RoleName.ROLE_ADMIN));

        User admin = User.builder()
                .fullName("Admin User")
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .enabled(true)
                .roles(Set.of(userRole, modRole, adminRole))
                .build();
        userRepository.save(admin);

        User user = User.builder()
                .fullName("Regular User")
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("user123"))
                .enabled(true)
                .roles(Set.of(userRole))
                .build();
        userRepository.save(user);

        Category electronics = categoryRepository.save(Category.builder()
                .name("Electronics").description("Electronic devices and gadgets").sortOrder(1).active(true).build());
        Category clothing = categoryRepository.save(Category.builder()
                .name("Clothing").description("Apparel and fashion").sortOrder(2).active(true).build());
        Category books = categoryRepository.save(Category.builder()
                .name("Books").description("Books and publications").sortOrder(3).active(true).build());

        Category smartphones = categoryRepository.save(Category.builder()
                .name("Smartphones").description("Mobile phones and accessories")
                .parentCategory(electronics).sortOrder(1).active(true).build());
        Category laptops = categoryRepository.save(Category.builder()
                .name("Laptops").description("Notebooks and ultrabooks")
                .parentCategory(electronics).sortOrder(2).active(true).build());

        productRepository.saveAll(List.of(
            Product.builder().name("iPhone 15 Pro").description("Latest Apple smartphone with A17 Pro chip")
                    .price(new BigDecimal("1299.99")).stockQuantity(50).sku("IPH-15P")
                    .featured(true).category(smartphones).active(true).build(),
            Product.builder().name("Samsung Galaxy S24").description("Samsung flagship with AI features")
                    .price(new BigDecimal("1099.99")).stockQuantity(40).sku("SGS-24")
                    .featured(true).category(smartphones).active(true).build(),
            Product.builder().name("MacBook Pro 16").description("Apple M3 Pro chip, 16-inch display")
                    .price(new BigDecimal("2499.99")).stockQuantity(20).sku("MBP-16")
                    .featured(true).category(laptops).active(true).build(),
            Product.builder().name("Dell XPS 15").description("Premium Windows laptop with OLED display")
                    .price(new BigDecimal("1899.99")).stockQuantity(15).sku("XPS-15")
                    .featured(true).category(laptops).active(true).build(),
            Product.builder().name("Cotton T-Shirt").description("Comfortable 100% cotton t-shirt")
                    .price(new BigDecimal("29.99")).stockQuantity(200).sku("CT-001")
                    .category(clothing).active(true).build(),
            Product.builder().name("Leather Jacket").description("Premium genuine leather jacket")
                    .price(new BigDecimal("199.99")).stockQuantity(30).sku("LJ-001")
                    .featured(true).category(clothing).active(true).build(),
            Product.builder().name("Spring Boot in Action").description("Comprehensive guide to Spring Boot")
                    .price(new BigDecimal("49.99")).stockQuantity(100).sku("BK-SB")
                    .category(books).active(true).build(),
            Product.builder().name("Clean Code").description("Robert C. Martin's classic")
                    .price(new BigDecimal("39.99")).stockQuantity(150).sku("BK-CC")
                    .category(books).active(true).build()
        ));

        log.info("Database seeding completed successfully");
    }
}

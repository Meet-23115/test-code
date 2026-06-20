package com.example.util;

public final class AppConstants {
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final String MAX_PAGE_SIZE = "100";
    public static final String DEFAULT_SORT = "createdAt";
    public static final String DEFAULT_SORT_DIR = "desc";

    public static final long JWT_EXPIRATION_MS = 86400000;
    public static final String TOKEN_TYPE = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private AppConstants() {}
}

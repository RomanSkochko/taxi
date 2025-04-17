package com.romanskochko.taxi.features.user.controller;

public interface UsersApi {
    String BY_ID = "/api/v1/users/{id}";
    String CREATE = "/api/v1/users";
    String UPDATE = "/api/v1/users/{id}";
    String DELETE = "/api/v1/users/{id}";
    String CHANGE_PASSWORD = "/api/v1/users/change-password";
}

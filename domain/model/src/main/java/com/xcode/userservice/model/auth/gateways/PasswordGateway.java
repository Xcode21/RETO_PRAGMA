package com.xcode.userservice.model.auth.gateways;

public interface PasswordGateway {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}

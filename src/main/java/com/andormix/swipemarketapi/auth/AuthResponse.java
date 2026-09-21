package com.andormix.swipemarketapi.auth;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String email,
        String displayName,
        String role
) {
}
package com.andormix.swipemarketapi.auth;

public record UserResponse(
        Long id,
        String email,
        String displayName,
        String role
) {
}
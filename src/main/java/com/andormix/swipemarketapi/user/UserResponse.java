package com.andormix.swipemarketapi.user;

public record UserResponse(
        Long id,
        String email,
        String displayName,
        String role
) {
}
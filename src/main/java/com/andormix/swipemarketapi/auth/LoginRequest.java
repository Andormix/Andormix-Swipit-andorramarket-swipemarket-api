package com.andormix.swipemarketapi.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//DTO - RECORD = FINAL DE LECTURA + VALDIDACIONES DE ENTRADA @
public record LoginRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
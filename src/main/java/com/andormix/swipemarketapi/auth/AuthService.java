package com.andormix.swipemarketapi.auth;

import com.andormix.swipemarketapi.security.AppUserPrincipal;
import com.andormix.swipemarketapi.security.JwtService;
import com.andormix.swipemarketapi.user.User;
import com.andormix.swipemarketapi.user.UserRepository;
import com.andormix.swipemarketapi.user.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.authentication.BadCredentialsException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered"
            );
        }

        User user = new User(
                normalizedEmail,
                passwordEncoder.encode(request.password()),
                request.displayName().trim(),
                UserRole.USER
        );

        User savedUser = userRepository.save(user);
        UserDetails principal = AppUserPrincipal.from(savedUser);

        return createAuthResponse(savedUser, principal);
    }

    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            request.password()
                    )
            );
        } catch (BadCredentialsException exception) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        return createAuthResponse(user, principal);
    }

    private AuthResponse createAuthResponse(
            User user,
            UserDetails principal
    ) {
        String token = jwtService.generateToken(principal);

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getRole().name()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
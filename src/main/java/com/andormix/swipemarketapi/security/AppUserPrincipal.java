package com.andormix.swipemarketapi.security;

import com.andormix.swipemarketapi.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Recordar de Spring academy usé la opción de Principal principal básica. En este crearemos el principal custom.
public class AppUserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final String displayName;
    private final String role;
    private final boolean enabled;

    private AppUserPrincipal(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.password = user.getPasswordHash();
        this.displayName = user.getDisplayName();
        this.role = user.getRole().name();
        this.enabled = user.isEnabled();
    }

    public static AppUserPrincipal from(User user) {
        return new AppUserPrincipal(user);
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRoleName() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
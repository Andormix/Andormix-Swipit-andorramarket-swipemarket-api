package com.andormix.swipemarketapi.security;

import com.andormix.swipemarketapi.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return userRepository.findByEmailIgnoreCase(username.trim())
                .map(AppUserPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found"
                ));
    }
}
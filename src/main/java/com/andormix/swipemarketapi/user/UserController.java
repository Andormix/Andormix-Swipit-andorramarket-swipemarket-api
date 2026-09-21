package com.andormix.swipemarketapi.user;

import com.andormix.swipemarketapi.security.AppUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public UserResponse getCurrentUser(
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        return new UserResponse(
                principal.getUserId(),
                principal.getUsername(),
                principal.getDisplayName(),
                principal.getRoleName()
        );
    }
}
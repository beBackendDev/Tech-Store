package net.myapplication.myapp.security.oauth2.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.myapplication.myapp.user.service.impl.UserDetailsImpl;

@Service
public class CurrentUserService {

    public UserDetailsImpl getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication == null
                        || !authentication.isAuthenticated()
        ) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        return (UserDetailsImpl)
                authentication.getPrincipal();
    }


    public Long getCurrentUserId() {

        return getCurrentUser()
                .getId();
    }
}
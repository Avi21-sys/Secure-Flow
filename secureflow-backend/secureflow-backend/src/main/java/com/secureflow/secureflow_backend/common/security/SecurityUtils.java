package com.secureflow.secureflow_backend.common.security;

import com.secureflow.secureflow_backend.auth.security.SecurityUser;
import com.secureflow.secureflow_backend.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static SecurityUser getCurrentSecurityUser() {
        Authentication authentication = getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof SecurityUser securityUser) {
            return securityUser;
        }

        throw new IllegalStateException("Principal is not of type SecurityUser");
    }

    public static User getCurrentUser() {
        return getCurrentSecurityUser().getUser();
    }

    public static Long getCurrentUserId() {
        return getCurrentSecurityUser().getId();
    }

    public static String getCurrentUserEmail() {
        return getCurrentSecurityUser().getUsername();
    }

    public static String getCurrentUserName() {
        return getCurrentSecurityUser().getName();
    }
}

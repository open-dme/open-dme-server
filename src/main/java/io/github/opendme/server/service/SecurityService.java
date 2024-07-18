package io.github.opendme.server.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public boolean hasPermission(String role) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt jwt){
                return jwt.getClaimAsStringList("groups").contains(role);
        }
        return false;
    }

}

package io.github.opendme.server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    public record Test(String name, List<String> roles){}

    @PostMapping
    public ResponseEntity<Test> check(Authentication authentication) {
        if(authentication.getPrincipal() instanceof Jwt jwt) {
            return ResponseEntity.ok(new Test(
                    jwt.getClaim("email"),
                    jwt.getClaimAsStringList("groups")
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

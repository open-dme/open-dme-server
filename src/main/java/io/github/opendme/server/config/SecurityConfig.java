package io.github.opendme.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;

@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Configuration
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {"/v3/api-docs/**", "/swagger-ui/**", "/api/auth/**"};


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // todo make it secure again
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);

        http

                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(AUTH_WHITELIST).permitAll();
                    registry.requestMatchers(
                            "/api/**"
                    ).authenticated();
                    registry.anyRequest().authenticated();
                });
        http.oauth2ResourceServer(oauth2ResourceServer -> {
                    oauth2ResourceServer.jwt(configurer -> {
                        configurer.jwtAuthenticationConverter(getJwtAuthenticationConverter());
                    });
                    oauth2ResourceServer.bearerTokenResolver(new DefaultBearerTokenResolver());
                }
        );
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return new MappingJwtGrantedAuthoritiesConverter();
    }
}

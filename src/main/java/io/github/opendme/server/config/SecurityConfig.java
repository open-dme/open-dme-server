package io.github.opendme.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

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
        http.oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(Customizer.withDefaults()));
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();

        client.setRequestEntityConverter(new OAuth2AuthorizationCodeGrantRequestEntityConverter());

        return client;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauthUserService() {
        DefaultOAuth2UserService service = new DefaultOAuth2UserService();
        service.setRequestEntityConverter(new OAuth2UserRequestEntityConverter());

        return service;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return new MappingJwtGrantedAuthoritiesConverter();
    }
}

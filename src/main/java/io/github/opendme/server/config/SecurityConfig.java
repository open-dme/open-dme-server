package io.github.opendme.server.config;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private static final String SAMURUS_BOT_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:33.0) Gecko/20100101 Firefox/33.0";


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .logout(logout -> {
                    logout.logoutSuccessUrl("/");
                })
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(
                            "/api/**"
                    ).authenticated();
                    registry
                            .requestMatchers("/**", "/oauth2/**").permitAll();
                });
        http.oauth2Login(configurer -> {
            configurer.tokenEndpoint(config -> {
                        config.accessTokenResponseClient(accessTokenResponseClient());
                    })
                    .userInfoEndpoint(config -> {
                        config.userService(oauthUserService());
                    });

        });
        return http.build();
    }


    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();

        client.setRequestEntityConverter(new OAuth2AuthorizationCodeGrantRequestEntityConverter() {
            @Override
            public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest oauth2Request) {
                return addUserAgentHeader(Objects.requireNonNull(super.convert(oauth2Request)));
            }
        });

        return client;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauthUserService() {
        DefaultOAuth2UserService service = new DefaultOAuth2UserService();

        service.setRequestEntityConverter(new OAuth2UserRequestEntityConverter() {
            @Override
            public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
                return addUserAgentHeader(
                        Objects.requireNonNull(super.convert(userRequest)));
            }
        });

        return service;
    }




   /* @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://0.0.0.0:8080", "*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    */

    private RequestEntity<?> addUserAgentHeader(RequestEntity<?> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(request.getHeaders());
        headers.add(HttpHeaders.USER_AGENT, SAMURUS_BOT_USER_AGENT);

        return new RequestEntity<>(request.getBody(), headers, request.getMethod(), request.getUrl());
    }


}

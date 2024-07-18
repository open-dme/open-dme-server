package io.github.opendme.server.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;


public class MappingJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String authorityPrefix = "ROLE_";

    private static final String GROUPS = "groups";
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Collection<String> tokenScopes = parseScopesClaim(jwt);
        if (tokenScopes.isEmpty()) {
            return Collections.emptyList();
        }

        return tokenScopes.stream()
                          .map(s -> this.authorityPrefix + s)
                          .map(SimpleGrantedAuthority::new)
                          .collect(Collectors.toCollection(HashSet::new));
    }

    protected Collection<String> parseScopesClaim(Jwt jwt) {

        Set<String> mappedAuthorities = new HashSet<>();

        if (jwt.hasClaim(REALM_ACCESS_CLAIM)) {
            Map<String, Collection<String>> realmAccess = jwt.getClaim(REALM_ACCESS_CLAIM);
            var roles = realmAccess.get(ROLES_CLAIM);
            mappedAuthorities.addAll(roles);
        }
        if (jwt.hasClaim(GROUPS)) {
            Collection<String> roles = jwt.getClaim(GROUPS);
            mappedAuthorities.addAll(roles);
        }
        return mappedAuthorities;
    }
}

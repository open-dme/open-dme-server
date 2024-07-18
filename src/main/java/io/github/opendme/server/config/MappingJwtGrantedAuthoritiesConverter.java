package io.github.opendme.server.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class MappingJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private String authorityPrefix = "SCOPE_";

    private static final String GROUPS = "groups";
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

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
            var realmAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
            var roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
            mappedAuthorities.addAll(roles);
        }
        if (jwt.hasClaim(GROUPS)) {
            Collection<String> roles = jwt.getClaim(GROUPS);
            mappedAuthorities.addAll(roles);
        }

        return mappedAuthorities;

    }
}

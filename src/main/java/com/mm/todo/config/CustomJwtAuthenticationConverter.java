package com.mm.todo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private JwtAuthenticationConverter jwtAuthenticationConverter;
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    public CustomJwtAuthenticationConverter() {
        this.jwtAuthenticationConverter = new JwtAuthenticationConverter();
        this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    }

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Get the "resource_access" claim
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return authorities; // Return empty authorities if claim is missing
        }

        // Get "static-app" access information
        Map<String, Object> staticApp = (Map<String, Object>) resourceAccess.get("static-app");
        if (staticApp == null) {
            return authorities; // Return empty authorities if "static-app" is missing
        }

        // Extract roles from "static-app"
        Object rolesObj = staticApp.get("roles");
        if (rolesObj == null) {
            return authorities; // Return empty authorities if "roles" is missing
        }

        // Safely map roles to a list
        List<String> roles;
        try {
            roles = new ObjectMapper().convertValue(rolesObj, new TypeReference<List<String>>() {
            });
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing roles: " + e.getMessage());
            return authorities; // Return empty authorities if parsing fails
        }

        // Convert roles to GrantedAuthority and add to the list
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return authorities;
    }

}

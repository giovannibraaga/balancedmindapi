package com.fiap.balancedmind.infra.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class FirebaseJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String uid = jwt.getClaimAsString("user_id");
        if (!StringUtils.hasText(uid)) {
            uid = jwt.getSubject();
        }

        String email = jwt.getClaimAsString("email");

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        JwtAuthenticationToken auth =
                new JwtAuthenticationToken(jwt, authorities, uid);

        auth.setDetails(email);

        return auth;
    }
}

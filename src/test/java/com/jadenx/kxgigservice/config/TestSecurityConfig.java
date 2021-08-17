package com.jadenx.kxgigservice.config;


import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@TestConfiguration
@Slf4j
public class TestSecurityConfig {

    static final String AUTH_TOKEN = "token";
    static final String SUB = "sub";
    static final String USER_AUTH_ID = "8b9ee60c-6102-4601-8530-041c01f4a6e9";

    @Bean
    public JwtDecoder jwtDecoder() {
        // This anonymous class needs for the possibility of using SpyBean in test methods
        // Lambda cannot be a spy with spring @SpyBean annotation!!!
        // Don't replace with lambda!!
        return new JwtDecoder() {
            @Override
            public Jwt decode(final String token) {
                return jwt(token);
            }
        };
    }

    @SneakyThrows
    public Jwt jwt(final String token) {
        var subValue = Optional.of(parse(token)
            .getJWTClaimsSet()
            .getClaim("sub"))
            .map(String::valueOf).orElse(USER_AUTH_ID);

        Map<String, Object> claims = Map.of(
            SUB, subValue
        );

        return new Jwt(
            AUTH_TOKEN,
            Instant.now(),
            Instant.now().plusSeconds(60),
            Map.of("alg", "none"),
            claims
        );
    }

    private JWT parse(final String token) {
        try {
            return JWTParser.parse(token);
        } catch (Exception var3) {
            log.trace("Failed to parse token", var3);
            throw new BadJwtException(String.format("An error occurred while attempting to decode the Jwt: %s",
                var3.getMessage()), var3);
        }
    }

}

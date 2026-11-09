package ru.stud;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import ru.stud.dto.UserResponse;
import ru.stud.model.UserRoles;
import ru.stud.tokens.JwtService;
import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {
    @Test
    void preservesClaimsAlgorithmAndLifetime() {
        JwtEncoder encoder = parameters -> {
            JwtClaimsSet claims = parameters.getClaims();
            assertThat(parameters.getJwsHeader().getAlgorithm()).isEqualTo(MacAlgorithm.HS256);
            assertThat(claims.getSubject()).isEqualTo("7");
            assertThat(claims.getClaimAsString("login")).isEqualTo("u");
            assertThat(claims.getClaimAsString("role")).isEqualTo("GENERAL");
            assertThat(Duration.between(claims.getIssuedAt(), claims.getExpiresAt())).isEqualTo(Duration.ofMinutes(10));
            assertThat(claims.getClaims()).containsOnlyKeys("sub", "iat", "exp", "login", "role");
            return new Jwt("token", Instant.now(), Instant.now().plusSeconds(600),
                    parameters.getJwsHeader().getHeaders(), claims.getClaims());
        };
        assertThat(new JwtService(encoder).generateToken(new UserResponse(7L, "u", UserRoles.GENERAL)))
                .isEqualTo("token");
    }
}

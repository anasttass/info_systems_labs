package ru.stud.tokens;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import ru.stud.dto.UserForm;
import ru.stud.model.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {
    private JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder encoder){
        this.jwtEncoder=encoder;
    }

    public String generateToken(UserForm user){
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .claim("login", user.getLogin())
                .claim("role",user.getRole().name())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue();
    }
}

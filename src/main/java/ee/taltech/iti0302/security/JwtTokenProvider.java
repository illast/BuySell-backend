package ee.taltech.iti0302.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenProvider {
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long TOKEN_DURATION = 3600000;

    public static String generateToken(String email, Integer id) {
        long currentTimeMs = System.currentTimeMillis();

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("id", id);

        log.info("Generating token");
        return Jwts.builder()
            .setSubject("subject")
            .addClaims(claims)
            .setIssuedAt(new Date(currentTimeMs))
            .setExpiration(new Date(currentTimeMs + TOKEN_DURATION))
            .signWith(key)
            .compact();
    }
}

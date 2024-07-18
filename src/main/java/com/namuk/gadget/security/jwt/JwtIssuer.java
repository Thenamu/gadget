package com.namuk.gadget.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@PropertySource("classpath:application-jwt.yml")
public class JwtIssuer {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationInSeconds;

    @Value("${jwt.access.header}")
    private String accessHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

    private static final String USERID_CLAIM = "id";

    private static final String USERNUMBER_CLAIM = "number";

    private static final String USERROLE_CLAIM = "role";

    // secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS512.key().build().getAlgorithm());

    public String createAccessToken(String id, String role) {

        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withClaim("id", id)
                .withClaim("role", role)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationInSeconds * 1000))
                .sign(Algorithm.HMAC512(secretKey));
//        return Jwts.builder()
//                .subject(ACCESS_TOKEN_SUBJECT)
//                .claim("id", id)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationInSeconds * 1000))
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
    }

    public Optional<String> isExpireToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            Date date = decodedJWT.getExpiresAt();
            if(date != null && date.before(new Date(System.currentTimeMillis()))) {
                System.out.println("Token is expire " + "Date = " + date);
                System.out.println("Current Date = " + new Date(System.currentTimeMillis()));
                return Optional.empty();
            }
            return Optional.ofNullable(decodedJWT.getClaim(USERID_CLAIM).asString());
        } catch (Exception e) {
            System.out.println("Token is invalid: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * 
     * StringUtils.hasText(authorization) return (@Nullable str != null && !str.isBlank());
     *
     * @param request
     * @return
     */
    public String extractAccessTokenFromRequest(HttpServletRequest request) {
        var authorization = request.getHeader(accessHeader);

        if(StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            String pureToken = authorization.split(" ")[1];
            System.out.println("pureToken = " + pureToken);
            return pureToken;
        } else {
            throw new IllegalArgumentException("Authorization header is missing or invalid.");
        }
        // return Optional.of(authorization.substring(7));
    }

    public Optional<String> extractId(String token) {
        return Optional.ofNullable(
                        JWT.require(Algorithm.HMAC512(secretKey))
                                .build()
                                .verify(token)
                                .getClaim(USERID_CLAIM)
                                .asString()
                )
                .or(Optional::empty);
    }

    public Optional<Long> extractNumber(String token) {
        return Optional.ofNullable(
                        JWT.require(Algorithm.HMAC512(secretKey))
                                .build()
                                .verify(token)
                                .getClaim(USERNUMBER_CLAIM)
                                .asLong()
                )
                .or(Optional::empty);
    }

    public Optional<String> extractSubjectClaim(String token) {
        return Optional.ofNullable(
                JWT.require(Algorithm.HMAC512(secretKey))
                        .build()
                        .verify(token)
                        .getSubject()
        )
                .or(Optional::empty);
    }

    public Optional<List<SimpleGrantedAuthority>> extractAuthorities(String accessToken) {
        var jwt = JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken);

        // 2. 'jwt' 객체에서 "role"라는 클레임을 가져옵니다.
        Claim roleClaim = jwt.getClaim(USERROLE_CLAIM);

        // 3. 클레임이 null이거나 존재하지 않는다면 Optional.empty()를 반환합니다.
        if (roleClaim.isNull() || roleClaim.isMissing()) {
            return Optional.empty();
        }

        String role = roleClaim.asString();
        return Optional.of(List.of(new SimpleGrantedAuthority(role)));
    }
}

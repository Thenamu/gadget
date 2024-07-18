//package com.namuk.gadget.controller.security.jwt;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.namuk.gadget.repository.member.UserRepository;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.crossstore.ChangeSetPersister;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//@Setter
//@Transactional
//@RequiredArgsConstructor
//@PropertySource("classpath:application-jwt.yml")
//public class JwtIssuerExImpl implements JwtIssuerEx{
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Value("${jwt.access.expiration}")
//    private Long accessTokenExpirationInSeconds;
//
//    @Value("${jwt.refresh.expiration}")
//    private Long refreshTokenExpirationInSeconds;
//
//    @Value("${jwt.access.header}")
//    private String accessHeader;
//
//    @Value("${jwt.refresh.header}")
//    private String refreshHeader;
//
//    private final UserRepository userRepository;
//
//    private final ObjectMapper objectMapper;
//
//    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
//
//    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
//
//    private static final String USERID_CLAIM = "id";
//
//    private static final String USERNAME_CLAIM = "username";
//
//    private static final String BEARER = "Bearer ";
//
//    /**
//     * 사용자의 id를 이용하여 액세스 토큰을 생성
//     *
//     * @param id 사용자의 id
//     * @return JWT를 생성하고 문자열로 반환
//     */
//    @Override
//    public String createAccessToken(String id) {
//        return JWT.create() // JWT 생성 메소드
//                // PayLoad 설정
//                .withSubject(ACCESS_TOKEN_SUBJECT)
//                // Instant.now()를 사용하는 방법도 있다.
//                // .withExpiresAt(Instant.now().plus(Duration.ofMinutes()))
//                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationInSeconds * 1000)) // 토큰 만료시간 15분
//                // .withArrayClaim() 배열 클레임도 설정 가능
//                // 클레임 설정
//                .withClaim(USERID_CLAIM, id)
//                .sign(Algorithm.HMAC512(secretKey));
//                /* 토큰의 페이로드에는 토큰에서 사용할 정보의 조각들인 클레임(Claim)이 담겨있다.
//                클레임은 등록된 클레임(Registered Claim), 공개 클레임(Public Claim),
//                비공개 클레임(Private Claim) 으로 나누어지며, key-value 형태로 존재 */
//    }
//
//    @Override
//    public String createRefreshToken() {
//        return JWT.create()
//                .withSubject(REFRESH_TOKEN_SUBJECT)
//                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationInSeconds * 1000))
//                .sign(Algorithm.HMAC512(secretKey));
//
//        // Spring Security에서 제공하는 Jwts.builder()를 사용하여 JWT를 생성
//        /* return Jwts.builder()
//                .subject(REFRESH_TOKEN_SUBJECT)
//                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationInSeconds * 1000))
//                .signWith(this.decodeSecretKey())
//                .compact(); */
//    }
//
//    @Override
//    public SecretKey decodeSecretKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    @Override
//    public void updateRefreshToken(String id, String refreshToken) {
//        userRepository.findByUserId(id)
//                .ifPresentOrElse(
//                        user -> user.updateRefreshToken(refreshToken),
//                        () -> new Exception("바보 회원 없으용")
//                );
//    }
//
//    @Override
//    public void destroyRefreshToken(String id) {
//        userRepository.findByUserId(id)
//                .ifPresentOrElse(
//                        user -> user.destroyRefreshToken(),
//                        () -> new Exception("바보 회원 없으용")
//                );
//    }
//
//    @Override
//    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        setAccessTokenHeader(response, accessToken);
//        setRefreshTokenHeader(response, refreshToken);
//
//        Map<String, String> accessTokenAndRefreshTokenMap = new HashMap<>();
//        accessTokenAndRefreshTokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
//        accessTokenAndRefreshTokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);
//    }
//
//    @Override
//    public void sendAccessToken(HttpServletResponse response, String accessToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        setAccessTokenHeader(response, accessToken);
//
//        Map<String, String> accessTokenMap = new HashMap<>();
//        accessTokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
//    }
//
//    /**
//     * HTTP 헤더에서 JWT Access Token을 추출
//     * 주어진 HTTP 요청의 "Authorization" 헤더에서 "Bearer"로 시작하는 부분을 찾아서 추출한다.
//     *
//     * @param request
//     * @return 필터(추출한 Access Token이 null이 아니고 Bearer 로 시작) 통과하면 Bearer 부분을 삭제하고 Optional로 반환, 아니면 빈 Optional 반환
//     */
//    @Override
//    public Optional<String> extractAccessTokenFromRequest(HttpServletRequest request) {
//        //
//        return Optional.ofNullable(request.getHeader(accessHeader))
//                .filter(accessToken -> accessToken.startsWith(BEARER))
//                .map(accessToken -> accessToken.replace(BEARER, ""))
//                .or(Optional::empty);
//
//        // if (StringUtils.hasText(request.getHeader(accessHeader)) && token.startsWith("Bearer ")) {
//        //
//        // }
//    }
//
//    /**
//     * HTTP 헤더에서 JWT Refresh Token을 추출
//     * 주어진 HTTP 요청의 "Authorization" 헤더에서 "Bearer"로 시작하는 부분을 찾아서 추출한다.
//     *
//     * @param request
//     * @return 필터(추출한 Refresh Token이 null이 아니고 Bearer 로 시작) 통과하면 Bearer 부분을 삭제하고 Optional로 반환, 아니면 빈 Optional 반환
//     */
//    @Override
//    public Optional<String> extractRefreshTokenFromRequest(HttpServletRequest request) {
//        return Optional.ofNullable(request.getHeader(refreshHeader))
//                .filter(refreshToken -> refreshToken.startsWith(BEARER))
//                .map(refreshToken -> refreshToken.replace(BEARER, ""))
//                .or(Optional::empty);
//    }
//
//    /**
//     * 액세스 토큰을 JWT 라이브러리를 사용하여 검증하고, 해당 토큰에 포함된 사용자 ID 클레임 값을 추출
//     *
//     * @param accessToken
//     * @return 주어진 액세스 토큰을 검증하고, 검증된 사용자 ID 클레임 값을 문자열로 변환해서 옵셔널로 반환, 사용자 ID 클레임이 Null 값이면, 빈 Optional을 반환
//     */
//    @Override
//    public Optional<String> extractId(String accessToken) {
//        return Optional.ofNullable(
//                // 디코딩에 필요한 설정, HMAC512 알고리즘을 사용하여 JWT를 검증
//                // 토큰의 서명의 유효성을 검사하는데 사용할 알고리즘이 있는 JWT verifier builder를 반환
//                JWT.require(Algorithm.HMAC512(secretKey))
//                        // 위 설정을 바탕으로 빌더 객체 생성, 반환된 빌더로 JWT verifier를 생성
//                        .build()
//                        // 주어진 accessToken 검증하고 디코딩, accessToken을 검증하고 유효하지 않다면 예외를 발생
//                        .verify(accessToken)
//                        // 검증된 accessToken 사용자 ID 클레임 값 추출
//                        .getClaim(USERID_CLAIM)
//                        // 추출한 클레임 값을 문자열로 변환
//                        .asString()
//                )
//                .or(Optional::empty);
//    }
//
////    @Override
////    public Optional<String> extractUserName(String accessToken) {
////        return Optional.ofNullable(
////                JWT.require(Algorithm.HMAC512(secretKey))
////                        .build()
////                        .verify(accessToken)
////                        .getClaim(USERNAME_CLAIM)
////                        .asString()
////                )
////                .or(Optional::empty);
////    }
//
//    @Override
//    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
//        response.setHeader(accessHeader, accessToken);
//    }
//
//    @Override
//    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
//        response.setHeader(refreshHeader, refreshToken);
//    }
//
//    @Override
//    public boolean isTokenValid(String tokens) {
//        try {
//            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(tokens);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}

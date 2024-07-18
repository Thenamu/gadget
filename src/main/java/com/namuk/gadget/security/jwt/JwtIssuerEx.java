//package com.namuk.gadget.controller.security.jwt;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import javax.crypto.SecretKey;
//import java.util.Optional;
//
//public interface JwtIssuerEx {
//
//    String createAccessToken(String id);
//
//    String createRefreshToken();
//
//    void updateRefreshToken(String id, String refreshToken);
//
//    void destroyRefreshToken(String id);
//
//    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);
//
//    void sendAccessToken(HttpServletResponse response, String accessToken);
//
//    Optional<String> extractAccessTokenFromRequest(HttpServletRequest request);
//
//    Optional<String> extractRefreshTokenFromRequest(HttpServletRequest request);
//
//    Optional<String> extractId(String accessToken);
//
//    // Optional<String> extractUserName(String accessToken);
//
//    void setAccessTokenHeader(HttpServletResponse response, String accessToken);
//
//    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);
//
//    boolean isTokenValid(String tokens);
//
//    SecretKey decodeSecretKey();
//
//
//}

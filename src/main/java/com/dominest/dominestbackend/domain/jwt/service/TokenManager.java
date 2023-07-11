//package com.dominest.dominestbackend.domain.jwt.service;
//
//
//import com.dominest.dominestbackend.domain.jwt.constant.GrantType;
//import com.dominest.dominestbackend.domain.jwt.constant.TokenType;
//import com.dominest.dominestbackend.domain.jwt.dto.TokenDto;
//import com.dominest.dominestbackend.global.exception.ErrorCode;
//import com.dominest.dominestbackend.global.exception.auth.NotValidTokenException;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Slf4j
//@Component
//public class TokenManager {
//
//    @Value("${token.access-token-expiration-time}")
//    private String accessTokenExpirationTime;
//
//    @Value("${token.refresh-token-expiration-time}")
//    private String refreshTokenExpirationTime;
//
//    @Value("${token.secret}")
//    private String tokenSecret;
//
//    public TokenDto createTokenDto(String email) {
//        Date accessTokenExpireTime = createAccessTokenExpireTime();
//        Date refreshTokenExpireTime = createRefreshTokenExpireTime();
//
//        String accessToken = createAccessToken(email, accessTokenExpireTime);
//        String refreshToken = createRefreshToken(email, refreshTokenExpireTime);
//        return TokenDto.builder()
//                .grantType(GrantType.BEARER.getType())
//                .accessToken(accessToken)
//                .accessTokenExpireTime(accessTokenExpireTime)
//                .refreshToken(refreshToken)
//                .refreshTokenExpireTime(refreshTokenExpireTime)
//                .build();
//    }
//
//    private Date createAccessTokenExpireTime() {
//        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
//    }
//
//    private Date createRefreshTokenExpireTime() {
//        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
//    }
//
//    private String createAccessToken(String email, Date expirationTime) {
//        String accessToken = Jwts.builder()
//                .setSubject(TokenType.ACCESS.name())                // 토큰 제목
//                .setAudience(email)                                 // 토큰 대상자
//                .setIssuedAt(new Date())                            // 토큰 발급 시간
//                .setExpiration(expirationTime)                      // 토큰 만료 시간
//        /**
//         *      Claim 에는 key-value 로 여러 값 저장 가능한 듯
//         *      이렇게 토큰 내부에 정보를 저장해두면
//         *      DB에서 정보를 조회해 Role을 확인하거나 할 필요가 없음. 토큰만 뜯어도 정보가 있어서.
//         */
////                .claim("role", role)                          // 유저 role.
//                .signWith(SignatureAlgorithm.HS512, tokenSecret)
//                .setHeaderParam("typ", "JWT")
//                .compact();
//        return accessToken;
//    }
//
//    private String createRefreshToken(String email, Date expirationTime) {
//        String refreshToken = Jwts.builder()
//                .setSubject(TokenType.REFRESH.name())               // 토큰 제목
//                .setAudience(email)                                 // 토큰 대상자
//                .setIssuedAt(new Date())                            // 토큰 발급 시간
//                .setExpiration(expirationTime)                      // 토큰 만료 시간
//                .signWith(SignatureAlgorithm.HS512, tokenSecret)
//                .setHeaderParam("typ", "JWT")
//                .compact();
//        return refreshToken;
//    }
//
//    public String getMemberEmail(String accessToken) {
//        String email;
//        try {
//            Claims claims = Jwts.parser().setSigningKey(tokenSecret)
//                    .parseClaimsJws(accessToken).getBody();
//            email = claims.getAudience();
//        } catch (Exception e){
//            e.printStackTrace();
//            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
//        }
//        return email;
//    }
//
//    public boolean validateToken(String token){
//        try {
//            Jwts.parser().setSigningKey(tokenSecret)
//                    .parseClaimsJws(token);
//            return true;
//        } catch(JwtException e) {  //토큰 변조
//            log.info("잘못된 jwt token", e);
//        } catch (Exception e){
//            log.info("jwt token 검증 중 에러 발생", e);
//        }
//        return false;
//    }
//
//    public Claims getTokenClaims(String token) {
//        Claims claims;
//        try {
//            claims = Jwts.parser().setSigningKey(tokenSecret)  //jwt 만들 때 사용했던 키
//                    .parseClaimsJws(token).getBody()
//            ;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
//        }
//        return claims;
//    }
//
//    public boolean isTokenExpired(Date tokenExpiredTime) {
//        Date now = new Date();
//        if(now.after(tokenExpiredTime)) { //토큰 만료된 경우
//            return true;
//        }
//        return false;
//    }
//
//    public String getTokenType(String token){
//        String tokenType;
//        try{
//            Claims claims = Jwts.parser().setSigningKey(tokenSecret)
//                    .parseClaimsJws(token).getBody();
//            tokenType = claims.getSubject();
//        } catch (Exception e){
//            e.printStackTrace();
//            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
//        }
//        return tokenType;
//    }
//
////    public String getRole(String token) {
////        Claims claims;
////        try {
////            claims = Jwts.parser().setSigningKey(tokenSecret)
////                    .parseClaimsJws(token).getBody();
////        } catch (Exception e) {
////            e.printStackTrace();
////            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
////        }
////
////        return (String) claims.get("role");
////    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

package com.dominest.dominestbackend.domain.jwt.service;


import com.dominest.dominestbackend.domain.jwt.constant.GrantType;
import com.dominest.dominestbackend.domain.jwt.constant.TokenType;
import com.dominest.dominestbackend.domain.jwt.dto.TokenDto;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.auth.NotValidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Slf4j
@Component
public class TokenManager {

    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;
    private final Key key;

    @Autowired
    public TokenManager(
              @Value("${token.secret}") String tokenSecret
            , @Value("${token.access-token-expiration-time}") long accessTokenExpirationTime
            , @Value("${token.refresh-token-expiration-time}") long refreshTokenExpirationTime) {
        // Base64 Decode. String to Bin
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public TokenDto createTokenDto(String email) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(email, accessTokenExpireTime);
        String refreshToken = createRefreshToken(email, refreshTokenExpireTime);
        return TokenDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    private Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + accessTokenExpirationTime);
    }

    private Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + refreshTokenExpirationTime);
    }

    private String createAccessToken(String email, Date expirationTime) {
        return Jwts.builder()
                .setSubject(TokenType.ACCESS.name())                // 토큰 제목
                .setAudience(email)                                 // 토큰 대상자
                .setIssuedAt(new Date())                         // 토큰 발급 시간
                .setExpiration(expirationTime)                // 토큰 만료 시간
        /*
         *      Claim 에는 Standard Claims 들을 제외하고도
         *      key-value 로 여러 값 저장 가능.
         */
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    private String createRefreshToken(String email, Date expirationTime) {
        return Jwts.builder()
                .setSubject(TokenType.REFRESH.name())               // 토큰 제목
                .setAudience(email)                                 // 토큰 대상자
                .setIssuedAt(new Date())                            // 토큰 발급 시간
                .setExpiration(expirationTime)                      // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    // Claims 파싱과 예외처리를 담당함.
    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

    public String getMemberEmail(String accessToken) {
        return getTokenClaims(accessToken).getAudience(); // aud == email
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token);
            return true;
            /* 검증 여부를 boolean 반환해야 하므로 예외상황에서 로그만 출력함.*/
        } catch (MalformedJwtException e) {
            log.info("잘못된 jwt token", e);
        } catch (JwtException e) {
            log.info("jwt token 검증 중 에러 발생", e);
        }
        return false;
    }

    public boolean isTokenExpired(Date tokenExpiredTime) {
        Date now = new Date();
        return now.after(tokenExpiredTime);
    }

    public String getTokenType(String token){
        Claims claims = getTokenClaims(token);
        return claims.getSubject();
    }
}
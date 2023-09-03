package com.dominest.dominestbackend.domain.jwt.service;


import com.dominest.dominestbackend.domain.jwt.constant.AuthScheme;
import com.dominest.dominestbackend.domain.jwt.constant.TokenType;
import com.dominest.dominestbackend.domain.jwt.dto.TokenDto;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.auth.JwtAuthException;
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

    private final long accessTokenExpMillis;
    private final long refreshTokenExpMillis;
    private final Key key;

    @Autowired
    public TokenManager(
              @Value("${token.secret}") String tokenSecret
            , @Value("${token.access-token-expiration-time}") long accessTokenExpMillis
            , @Value("${token.refresh-token-expiration-time}") long refreshTokenExpMillis) {
        // Base64 Decode. String to Bin
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpMillis = accessTokenExpMillis;
        this.refreshTokenExpMillis = refreshTokenExpMillis;
    }

    public TokenDto createTokenDto(String audience) {
        Date accessTokenExp = createAccessTokenExp();
        Date refreshTokenExp = createRefreshTokenExp();

        String accessToken = createAccessToken(audience, accessTokenExp);
        String refreshToken = createRefreshToken(audience, refreshTokenExp);
        return TokenDto.builder()
                .authScheme(AuthScheme.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExp(accessTokenExp)
                .refreshToken(refreshToken)
                .refreshTokenExp(refreshTokenExp)
                .build();
    }

    // 테스트용 14일 유효 토큰 쌍 생성
    public TokenDto createTokenDtoTemp(String audience, Date tokenExp) {
        String accessToken = createAccessToken(audience, tokenExp);
        String refreshToken = createRefreshToken(audience, tokenExp);
        return TokenDto.builder()
                .authScheme(AuthScheme.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExp(tokenExp)
                .refreshToken(refreshToken)
                .refreshTokenExp(tokenExp)
                .build();
    }

    private Date createAccessTokenExp() {
        return new Date(System.currentTimeMillis() + accessTokenExpMillis);
    }

    private Date createRefreshTokenExp() {
        return new Date(System.currentTimeMillis() + refreshTokenExpMillis);
    }

    private String createAccessToken(String audience, Date exp) {
        return Jwts.builder()
                .setSubject(TokenType.ACCESS.name())                // 토큰 제목
                .setAudience(audience)                                 // 토큰 대상자
                .setIssuedAt(new Date())                         // 토큰 발급 시간
                .setExpiration(exp)                // 토큰 만료 시간
        /*
         *      Claim 에는 Standard Claims 들을 제외하고도
         *      key-value 로 여러 값 저장 가능.
         */
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    private String createRefreshToken(String audience, Date exp) {
        return Jwts.builder()
                .setSubject(TokenType.REFRESH.name())               // 토큰 제목
                .setAudience(audience)                                 // 토큰 대상자
                .setIssuedAt(new Date())                            // 토큰 발급 시간
                .setExpiration(exp)                      // 토큰 만료 시간
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
            throw new JwtAuthException(ErrorCode.NOT_VALID_TOKEN);
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

    public boolean isTokenExpired(Date exp) {
        Date now = new Date();
        return now.after(exp);
    }

    public String getTokenType(String token){
        Claims claims = getTokenClaims(token);
        return claims.getSubject();
    }
}
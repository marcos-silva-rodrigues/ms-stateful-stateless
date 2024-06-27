package com.rodrigues.silva.marcos.ms.auth_api.core.service;

import com.rodrigues.silva.marcos.ms.auth_api.core.model.User;
import com.rodrigues.silva.marcos.ms.auth_api.infra.exception.AuthenticationException;
import com.rodrigues.silva.marcos.ms.auth_api.infra.exception.ValidationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JwtService {

  private static final String EMPTY_SPACE = " ";
  private static final Integer TOKEN_INDEX = 1;
  private static final Integer ONE_DAY_IN_HOURS = 24;

  @Value("${app.token.secret-key")
  private String secreyKey;

  public String createToken(User user) {
    var data = new HashMap<String, String>();
    data.put("id", user.getId().toString());
    data.put("username", user.getUsername());

    return Jwts.builder()
            .claims(data)
            .expiration(generationExpiresAt())
            .signWith(generateSign())
            .compact();
  }

  private SecretKey generateSign() {
    return Keys.hmacShaKeyFor(secreyKey.getBytes());
  }

  private Date generationExpiresAt() {
    return Date.from(
            LocalDateTime.now()
                    .plusHours(ONE_DAY_IN_HOURS)
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
  }

  public void validateAccessToken(String token) {
    var accessToken = extractToken(token);
    try {
      Jwts.parser()
              .verifyWith(generateSign())
              .build()
              .parseSignedClaims(accessToken)
              .getPayload();
    } catch (Exception ex) {
      throw new AuthenticationException("Invalid token " + ex.getMessage());
    }
  }

  private String extractToken(String token) {
    if (isEmpty(token)) {
      throw new ValidationException("the access token was not informed");
    }

    if (token.contains(EMPTY_SPACE)) {
      return token.split(EMPTY_SPACE)[TOKEN_INDEX];
    }

    return token;
  }
}

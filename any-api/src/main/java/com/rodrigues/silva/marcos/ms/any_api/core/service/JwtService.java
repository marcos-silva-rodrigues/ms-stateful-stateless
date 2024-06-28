package com.rodrigues.silva.marcos.ms.any_api.core.service;

import com.rodrigues.silva.marcos.ms.any_api.core.dto.AuthUserResponse;
import com.rodrigues.silva.marcos.ms.any_api.infra.exception.AuthenticationException;
import com.rodrigues.silva.marcos.ms.any_api.infra.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JwtService {

  private static final String EMPTY_SPACE = " ";
  private static final Integer TOKEN_INDEX = 1;

  @Value("${app.token.secret-key}")
  private String secretKey;

  private SecretKey generateSign() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public AuthUserResponse getAuthenticatedUser(String token ) {
    Claims tokenClaims = getClaims(token);
    var userId = Integer.valueOf((String) tokenClaims.get("id"));
    var username = (String) tokenClaims.get("username");

    return new AuthUserResponse(userId, username);
  }

  public void validateAccessToken(String token) {
    getClaims(token);
  }

  public Claims getClaims(String token) {
    var accessToken = extractToken(token);
    try {
      return Jwts.parser()
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

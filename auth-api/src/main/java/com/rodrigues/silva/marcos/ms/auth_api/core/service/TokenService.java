package com.rodrigues.silva.marcos.ms.auth_api.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigues.silva.marcos.ms.auth_api.core.dto.TokenData;
import com.rodrigues.silva.marcos.ms.auth_api.infra.exception.AuthenticationException;
import com.rodrigues.silva.marcos.ms.auth_api.infra.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Profile("stateful")
@Service
@AllArgsConstructor
@Slf4j
public class TokenService {

  private static final String EMPTY_SPACE = " ";
  private static final Integer TOKEN_INDEX = 1;

  // 24 (DAY) x 60 (MINUTE) x 60 (SECOND)
  private static final Long ONE_DAY_IN_SECONDS = 86400L;

  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;

  public String createToken(String username) {
    var accessToken = UUID.randomUUID().toString();
    var data = new TokenData(username);

    var jsonData = getJsonData(data);
    redisTemplate.opsForValue().set(accessToken, jsonData);
    redisTemplate.expireAt(accessToken, Instant.now().plusSeconds(ONE_DAY_IN_SECONDS));
    return accessToken;
  }

  private String getJsonData(Object payload) {
    log.info("Parse json data");
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (Exception ex) {
        log.error(ex.getMessage());
        return "";
    }
  }

  public TokenData getTokenData(String token) {
    var accessToken = extractToken(token);
    var jsonString = getRedisTokenValue(accessToken);

    try {
      return objectMapper.readValue(jsonString, TokenData.class);
    } catch (Exception ex) {
      throw  new AuthenticationException("Error extracting the authenticated user: " + ex.getMessage());
    }
  }

  public boolean validateAccessToken(String token) {
    var accessToken = extractToken(token);
    var data = getRedisTokenValue(accessToken);
    return !isEmpty(data);
  }

  private String getRedisTokenValue(String token) {
    return redisTemplate.opsForValue().get(token);
  }

  public void deleteRedisToken(String token) {
    var accessToken = extractToken(token);
    redisTemplate.delete(accessToken);
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

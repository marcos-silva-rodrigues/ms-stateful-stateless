package com.rodrigues.silva.marcos.ms.any_api.core.service;

import com.rodrigues.silva.marcos.ms.any_api.core.client.TokenClient;
import com.rodrigues.silva.marcos.ms.any_api.core.dto.AuthUserResponse;
import com.rodrigues.silva.marcos.ms.any_api.infra.exception.AuthenticationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("stateful")
@Slf4j
@Service
@AllArgsConstructor
public class TokenService implements AuthService {

  private final TokenClient tokenClient;

  @Override
  public void validateToken(String token) {
    try {
      log.info("Sending request for token validation {}", token);
      var response = tokenClient.validateToken(token);
      log.info("Token is valid: {}", response.accessToken());
    } catch (Exception ex) {
      log.error(ex.getMessage());
      throw new AuthenticationException("Auth error: " + ex.getMessage());
    }
  }

  @Override
  public AuthUserResponse getAuthenticatedUser(String token) {
    try {
      log.info("Sending request for auth user: {}", token);
      var response = tokenClient.getAuthenticatedUser(token);
      log.info("Auth user found: {} and token {}", response.toString(), token);

      return response;
    } catch (Exception ex) {
      log.error(ex.getMessage());
      throw new AuthenticationException("Auth to get authenticated user: " + ex.getMessage());
    }
  }
}

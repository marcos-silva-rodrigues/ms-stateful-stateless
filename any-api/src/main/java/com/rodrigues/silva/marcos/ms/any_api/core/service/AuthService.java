package com.rodrigues.silva.marcos.ms.any_api.core.service;

import com.rodrigues.silva.marcos.ms.any_api.core.dto.AuthUserResponse;

public interface AuthService {

  void validateToken(String token);
  AuthUserResponse getAuthenticatedUser(String token);
}

package com.rodrigues.silva.marcos.ms.any_api.core.service;

import com.rodrigues.silva.marcos.ms.any_api.core.dto.AnyResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnyService {

  private final AuthService authService;

  public AnyResponse getData(String accessToken) {
    authService.validateToken(accessToken);
    var authUser = authService.getAuthenticatedUser(accessToken);
    var ok = HttpStatus.OK;

    return new AnyResponse(ok.name(), ok.value(), authUser);
  }
}

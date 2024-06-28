package com.rodrigues.silva.marcos.ms.any_api.core.service;

import com.rodrigues.silva.marcos.ms.any_api.core.client.TokenClient;
import com.rodrigues.silva.marcos.ms.any_api.core.dto.AnyResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatefulAnyService {

  private final TokenService tokenService;

  public AnyResponse getData(String accessToken) {
    tokenService.validateToken(accessToken);
    var authUser = tokenService.getAuthenticatedUser(accessToken);
    var ok = HttpStatus.OK;

    return new AnyResponse(ok.name(), ok.value(), authUser);
  }
}

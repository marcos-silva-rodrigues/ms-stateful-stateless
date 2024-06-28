package com.rodrigues.silva.marcos.ms.any_api.core.client;

import com.rodrigues.silva.marcos.ms.any_api.core.dto.AuthUserResponse;
import com.rodrigues.silva.marcos.ms.any_api.core.dto.TokenDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@Profile("stateful")
@HttpExchange("api/v1/auth")
public interface TokenClient {

  @PostExchange("token/validate")
  TokenDTO validateToken(@RequestHeader String accessToken);

  @GetExchange("user")
  AuthUserResponse getAuthenticatedUser(@RequestHeader String accessToken);

}

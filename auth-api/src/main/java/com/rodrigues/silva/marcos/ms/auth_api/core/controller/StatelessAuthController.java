package com.rodrigues.silva.marcos.ms.auth_api.core.controller;

import com.rodrigues.silva.marcos.ms.auth_api.core.dto.AuthRequest;
import com.rodrigues.silva.marcos.ms.auth_api.core.dto.TokenDTO;
import com.rodrigues.silva.marcos.ms.auth_api.core.service.StatelessAuthService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@Profile("stateless")
@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class StatelessAuthController {

  private final StatelessAuthService statelessAuthService;

  @PostMapping("login")
  public TokenDTO login(@RequestBody AuthRequest authRequest) {
    return statelessAuthService.login(authRequest);
  }

  @PostMapping("token/validate")
  public TokenDTO validate(@RequestHeader("token") String accessToken) {
    return statelessAuthService.validateToken(accessToken);
  }
}

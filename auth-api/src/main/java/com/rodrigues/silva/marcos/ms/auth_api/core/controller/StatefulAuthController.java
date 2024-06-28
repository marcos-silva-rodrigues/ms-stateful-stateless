package com.rodrigues.silva.marcos.ms.auth_api.core.controller;

import com.rodrigues.silva.marcos.ms.auth_api.core.dto.AuthRequest;
import com.rodrigues.silva.marcos.ms.auth_api.core.dto.AuthUserResponse;
import com.rodrigues.silva.marcos.ms.auth_api.core.dto.TokenDTO;
import com.rodrigues.silva.marcos.ms.auth_api.core.service.AuthService;
import com.rodrigues.silva.marcos.ms.auth_api.core.service.OpaqueTokenAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Profile("stateful")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class StatefulAuthController {

  private final OpaqueTokenAuthService service;

  @PostMapping("login")
  public TokenDTO login(@RequestBody AuthRequest authRequest) {
    return service.login(authRequest);
  }

  @PostMapping("token/validate")
  public TokenDTO validate(@RequestHeader String accessToken) {
    return service.validateToken(accessToken);
  }

  @PostMapping("logout")
  public HashMap<String, Object> logout(@RequestHeader String accessToken) {
    service.logout(accessToken);
    var ok = HttpStatus.OK;
    var response = new HashMap<String, Object>() {{
      put("status", ok.name());
      put("code" , ok.value());
    }};

    return response;
  }

  @GetMapping("user")
  public AuthUserResponse getAuthenticatedUser(@RequestHeader String accessToken) {
    return service.getAuthenticatedUser(accessToken);
  }
}

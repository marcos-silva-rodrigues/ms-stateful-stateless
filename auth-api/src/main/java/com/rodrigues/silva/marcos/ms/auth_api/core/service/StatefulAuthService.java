package com.rodrigues.silva.marcos.ms.auth_api.core.service;

import com.rodrigues.silva.marcos.ms.auth_api.core.dto.AuthRequest;
import com.rodrigues.silva.marcos.ms.auth_api.core.dto.AuthUserResponse;
import com.rodrigues.silva.marcos.ms.auth_api.core.dto.TokenDTO;
import com.rodrigues.silva.marcos.ms.auth_api.core.model.User;
import com.rodrigues.silva.marcos.ms.auth_api.core.repository.UserRepository;
import com.rodrigues.silva.marcos.ms.auth_api.infra.exception.AuthenticationException;
import com.rodrigues.silva.marcos.ms.auth_api.infra.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Profile("stateful")
@Service
@AllArgsConstructor
public class StatefulAuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  public TokenDTO login(AuthRequest authRequest) {
    var user = findByUsername(authRequest.username());

    validatePassword(authRequest.password(), user.getPassword());

    var accessToken = tokenService.createToken(user.getUsername());
    return new TokenDTO(accessToken);
  }

  private User findByUsername(String username) {
    var user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new ValidationException("User not found"));
    return user;
  }

  private void validateExistingToken(String accessToken) {
    if(isEmpty(accessToken)) {
      throw new ValidationException("The access token must be informed!");
    }
  }

  private void validatePassword(String rawPassword, String encodePassword) {
    if(!passwordEncoder.matches(rawPassword, encodePassword))
      throw new ValidationException("Password or Username is incorrect");
  }

  public TokenDTO validateToken(String accessToken) {
    validateExistingToken(accessToken);
    var valid = tokenService.validateAccessToken(accessToken);
    if(valid) {
      return new TokenDTO(accessToken);
    }

    throw new AuthenticationException("Invalid token!");
  }

  public AuthUserResponse getAuthenticatedUser(String accessToken) {
    var tokenData = tokenService.getTokenData(accessToken);
    var user = findByUsername(tokenData.username());
    return new AuthUserResponse(user.getId(), user.getUsername());
  }

  public void logout(String accessToken) {
    tokenService.deleteRedisToken(accessToken);
  }
}

package com.rodrigues.silva.marcos.ms.auth_api.core.service;

import com.rodrigues.silva.marcos.ms.auth_api.core.dto.AuthRequest;
import com.rodrigues.silva.marcos.ms.auth_api.core.dto.TokenDTO;
import com.rodrigues.silva.marcos.ms.auth_api.core.repository.UserRepository;
import com.rodrigues.silva.marcos.ms.auth_api.infra.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtService jwtService;

  public TokenDTO login(AuthRequest authRequest) {
    var user = userRepository
            .findByUsername(authRequest.username())
            .orElseThrow(() -> new ValidationException("User not found"));

    validatePassword(authRequest.password(), user.getPassword());

    var accessToken = jwtService.createToken(user);
    return new TokenDTO(accessToken);
  }

  private void validatePassword(String rawPassword, String encodePassword) {
    if(!passwordEncoder.matches(rawPassword, encodePassword))
      throw new ValidationException("Password or Username is incorrect");
  }

  public TokenDTO validateToken(String accessToken) {
    validateExistingToken(accessToken);
    jwtService.validateAccessToken(accessToken);
    return new TokenDTO(accessToken);
  }

  private void validateExistingToken(String accessToken) {
    if(isEmpty(accessToken)) {
      throw new ValidationException("The access token must be informed!");
    }
  }
}

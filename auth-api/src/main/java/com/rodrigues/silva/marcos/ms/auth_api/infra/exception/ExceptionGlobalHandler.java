package com.rodrigues.silva.marcos.ms.auth_api.infra.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class ExceptionGlobalHandler {

  @ExceptionHandler(ValidationException.class)
  public ProblemDetail handle(ValidationException ex) {
    var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problem.setTitle("Validation Error");
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handle(AuthenticationException ex) {
    var problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    problem.setTitle("Authentication Error");
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }
}

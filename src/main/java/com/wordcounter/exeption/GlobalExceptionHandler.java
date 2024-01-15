package com.wordcounter.exeption;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.wordcounter.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(WordCountServiceException.class)
  @Order(HIGHEST_PRECEDENCE)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleAdapterException(WordCountServiceException exception) {
    ErrorType errorMessage = exception.getErrorType();
    HttpStatus httpStatus = errorMessage.getHttpStatus();
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .description(errorMessage.getDescription())
            .build(),
        httpStatus);
  }

  @ExceptionHandler(Exception.class)
  @Order(HIGHEST_PRECEDENCE)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleException(Exception exception) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .description(exception.getMessage())
            .build(), INTERNAL_SERVER_ERROR);
  }
}

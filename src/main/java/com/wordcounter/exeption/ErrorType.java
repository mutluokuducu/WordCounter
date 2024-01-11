package com.wordcounter.exeption;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public enum ErrorType {
  INTERNAL_ERROR("An internal server error occurred", INTERNAL_SERVER_ERROR),
  INVALID("Invalid request", BAD_REQUEST);


  private String description;
  private HttpStatus httpStatus;

  ErrorType(String description, HttpStatus httpStatus) {

    this.description = description;
    this.httpStatus = httpStatus;
  }
}

package com.wordcounter.exeption;

import lombok.Getter;

@Getter
public class WordCountServiceException extends RuntimeException {

  private final ErrorType errorType;

  public WordCountServiceException(ErrorType errorType) {
    this.errorType = errorType;
  }
}

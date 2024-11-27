package com.ocr.p3back.exception.auth;

public class DuplicationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public DuplicationException(String message) {
    super(message);
  }

}

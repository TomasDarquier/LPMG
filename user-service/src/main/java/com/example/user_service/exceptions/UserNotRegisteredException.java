package com.example.user_service.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserNotRegisteredException extends RuntimeException {
  private final String message;
}

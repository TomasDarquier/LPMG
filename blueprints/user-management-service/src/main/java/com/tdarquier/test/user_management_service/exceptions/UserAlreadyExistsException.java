package com.tdarquier.test.user_management_service.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAlreadyExistsException extends RuntimeException {
    private final String message;
}

package com.tdarquier.test.user_management_service.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidUserException extends RuntimeException {
    private final String message;
}

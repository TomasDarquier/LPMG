package com.example.user_service.handlers;

import java.util.Map;

public record ErrorResponse(
        Map<String,String> errors
) {
}

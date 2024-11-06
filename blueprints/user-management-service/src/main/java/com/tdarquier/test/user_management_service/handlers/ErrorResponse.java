package com.tdarquier.test.user_management_service.handlers;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {}
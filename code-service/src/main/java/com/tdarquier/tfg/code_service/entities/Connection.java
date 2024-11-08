package com.tdarquier.tfg.code_service.entities;

import com.tdarquier.tfg.code_service.enums.ConnectionType;
import com.tdarquier.tfg.code_service.enums.Template;

public record Connection(
        ConnectionType type,
        Template connectedTo,
        int port,
        String apiPath
) {}

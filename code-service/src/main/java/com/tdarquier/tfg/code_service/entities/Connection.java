package com.tdarquier.tfg.code_service.entities;

import com.tdarquier.tfg.code_service.enums.ConnectionProtocol;
import com.tdarquier.tfg.code_service.enums.ConnectionType;

public record Connection(ConnectionType type, ConnectionProtocol protocol) {
}

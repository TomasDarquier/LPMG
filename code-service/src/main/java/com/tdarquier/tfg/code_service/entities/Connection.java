package com.tdarquier.tfg.code_service.entities;

import com.tdarquier.tfg.code_service.enums.ConnectionType;
import com.tdarquier.tfg.code_service.enums.Template;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
    ConnectionType type;
    Template connectedTo;
    int port;
    String apiPath;
}

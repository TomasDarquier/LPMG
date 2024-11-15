package com.tdarquier.tfg.code_service.entities;

import com.tdarquier.tfg.code_service.enums.PersistenceType;
import com.tdarquier.tfg.code_service.enums.Template;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ComponentData {

        private Template template;
        private String name;
        private List<Connection> connections;
        private Map<String, String> paths;
        private Integer port;
        private String apiPath;
        private Boolean isConfigServerEnabled;
        private Boolean isGatewayEnabled;
        private PersistenceType persistenceType;

}

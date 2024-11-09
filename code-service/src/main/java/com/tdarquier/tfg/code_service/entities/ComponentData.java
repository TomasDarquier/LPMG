package com.tdarquier.tfg.code_service.entities;

import com.tdarquier.tfg.code_service.enums.PersistenceType;
import com.tdarquier.tfg.code_service.enums.Template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComponentData {

        private Template template;
        private String name;
        private List<Connection> connections;
        private Map<String, String> paths;
        private Integer port;
        private String apiPath;
        private Boolean isConfigServerEnabled;
        private PersistenceType persistenceType;

}

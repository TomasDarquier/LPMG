package com.tdarquier.init_service.entities;

import com.tdarquier.init_service.enums.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
    private String name;
    private String groupId;
    private String artifactId;
    private String dependencies;
    private Template template;
}

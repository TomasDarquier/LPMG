package com.tdarquier.tfg.code_service.services;

import java.util.Map;

public interface ProjectStructureService {
    /**
     * Crea la estructura de carpetas para un proyecto dado.
     *
     * @param projectBucket El nombre del bucket en Minio
     * @param groupId El identificador del grupo
     * @param artifactId El identificador del artefacto
     * @return Un mapa con las rutas creadas
     */
    Map<String, String> createFolderStructure(String projectBucket, String groupId, String artifactId);

    /**
     * Crea los archivos necesarios para el control de versiones en el proyecto (e.g., .gitignore, .gitattributes).
     *
     * @param path El path donde se deben guardar los archivos
     * @param bucket El nombre del bucket en Minio
     */
    void createGitFiles(String path, String bucket);

    /**
     * Crea los archivos de configuración de Maven (e.g., mvnw, mvnw.cmd).
     *
     * @param path El path donde se deben guardar los archivos
     * @param bucket El nombre del bucket en Minio
     */
    void createMavenFiles(String path, String bucket);

    /**
     * Crea el archivo pom.xml en la ubicación especificada.
     *
     * @param path El path donde se debe guardar el archivo
     * @param pom El contenido del archivo pom.xml
     * @param bucket El nombre del bucket en Minio
     */
    void createPom(String path, String pom, String bucket);
}

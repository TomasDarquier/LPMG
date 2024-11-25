package com.tdarquier.tfg.code_service.services;

import com.tdarquier.tfg.code_service.entities.MinioFile;

public interface MinioService {
    /**
     * Crea un nuevo bucket en Minio con el nombre especificado.
     *
     * @param name El nombre del bucket a crear.
     * @throws Exception Si ocurre algún error durante la creación del bucket.
     */
    void createNewBucket(String name) throws Exception;

    /**
     * Verifica si un bucket existe en Minio.
     *
     * @param path El nombre del bucket a verificar.
     * @return true si el bucket existe, false en caso contrario.
     * @throws Exception Si ocurre algún error durante la verificación del bucket.
     */
    boolean bucketExists(String path) throws Exception;

    /**
     * Guarda un archivo en un bucket de Minio.
     *
     * @param minioFile El archivo a subir.
     */
    void saveObject(MinioFile minioFile);

    /**
     * Obtiene un archivo desde Minio.
     *
     * @param minioFile El archivo a obtener.
     * @return El contenido del archivo en bytes, o null si ocurre un error.
     */
    byte[] getObject(MinioFile minioFile);
}

package com.tdarquier.tfg.download_service.services;

import com.tdarquier.tfg.download_service.dtos.ZipFileResponse;

import java.util.List;

public interface MinioService {
    /**
     * Lista los buckets que coinciden con el ID proporcionado.
     *
     * @param id el identificador que debe estar al principio del nombre del bucket
     * @return una lista de nombres de buckets que coinciden con el ID
     */
    List<String> listBucketsById(Long id);

    /**
     * Obtiene un archivo ZIP que contiene todos los objetos dentro de un bucket dado.
     * Los objetos se listan de manera recursiva y se descargan para ser comprimidos en el archivo ZIP.
     *
     * @param bucket el nombre del bucket del cual se generará el archivo ZIP
     * @return un objeto `ZipFileResponse` que contiene el nombre del archivo ZIP, su tamaño y los datos comprimidos
     */
    ZipFileResponse getZip(String bucket);

    /**
     * Calcula el tamaño total de todos los archivos dentro de un bucket.
     *
     * @param bucketName el nombre del bucket del cual se calculará el tamaño
     * @return el tamaño total del bucket en formato legible en bytes o kilobytes)
     */
    String getSize(String bucketName);
}

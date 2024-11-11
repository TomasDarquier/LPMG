package com.tdarquier.tfg.download_service.services;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class MinioService {

    final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public List<String> listBucketsById(Long id){
        List<String> matchingBuckets = new ArrayList<>();
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            for (Bucket bucket : buckets) {
                String bucketName = bucket.name();
                if (bucketName.startsWith(id + "-")) {
                    matchingBuckets.add(bucketName);
                    System.out.println("Bucket encontrado: " + bucketName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar los buckets: " + e.getMessage());
            e.printStackTrace();
        }
        return matchingBuckets;
    }

    public InputStreamResource getZip(String bucket) throws IOException, MinioException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {

            // Listar y descargar archivos recursivamente desde el bucket
            Iterable<Result<Item>> items = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucket)
                    .recursive(true) // Permite listar recursivamente
                    .build());

            boolean hasFiles = false; // Indicador para saber si realmente se añadieron archivos

            for (Result<Item> result : items) {
                Item item = result.get();
                String objectName = item.objectName();

                // Ignorar directorios (objetos con nombre que termina en "/")
                if (objectName.endsWith("/")) {
                    System.out.println("Ignorando directorio: " + objectName);
                    continue;
                }

                // Descargar archivo y verificar su contenido
                byte[] objectData = downloadFileFromBucket(bucket, objectName);
                if (objectData.length > 0) {
                    hasFiles = true;
                    // Agregar archivo al ZIP
                    ZipEntry zipEntry = new ZipEntry(objectName);
                    zos.putNextEntry(zipEntry);
                    zos.write(objectData);
                    zos.closeEntry();
                    System.out.println("Añadido al ZIP: " + objectName);
                } else {
                    System.out.println("Archivo vacío o no encontrado: " + objectName);
                }
            }

            if (!hasFiles) {
                System.out.println("No se encontraron archivos para añadir al ZIP.");
            }
        } catch (Exception e) {
            System.err.println("Error al crear el ZIP: " + e.getMessage());
            e.printStackTrace();
        }

        return new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));
    }

    private byte[] downloadFileFromBucket(String bucket, String objectName) throws IOException, MinioException {
        try (InputStream is = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .build())) {
            byte[] data = is.readAllBytes();
            System.out.println("Descargado: " + objectName + " (" + data.length + " bytes)");
            return data;
        } catch (Exception e) {
            System.err.println("Error al descargar " + objectName + ": " + e.getMessage());
            return new byte[0];
        }
    }

    public String getSize(String bucketName) {
        long totalSize = 0;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).recursive(true).build());

            for (Result<Item> result : results) {
                Item item = result.get();
                totalSize += item.size();
            }

            System.out.println("Tamaño total del bucket '" + bucketName + "': " + totalSize + " bytes");
        } catch (Exception e) {
            System.err.println("Error al calcular el tamaño del bucket: " + e.getMessage());
            e.printStackTrace();
        }
        return totalSize + " bytes";
    }
}
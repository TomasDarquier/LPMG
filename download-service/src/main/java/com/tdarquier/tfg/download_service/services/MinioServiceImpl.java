package com.tdarquier.tfg.download_service.services;

import com.tdarquier.tfg.download_service.dtos.ZipFileResponse;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RequiredArgsConstructor
@Service
public class MinioServiceImpl implements MinioService{

    private final MinioClient minioClient;

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

    public ZipFileResponse getZip(String bucket){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {

            // Listar y descargar archivos recursivamente desde el bucket
            Iterable<Result<Item>> items = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucket)
                    .recursive(true)
                    .build());

            for (Result<Item> result : items) {
                Item item = result.get();
                String objectName = item.objectName();

                if (!objectName.endsWith("/")) { // Ignorar directorios
                    byte[] objectData = downloadFileFromBucket(bucket, objectName);
                    if (objectData.length > 0) {
                        ZipEntry zipEntry = new ZipEntry(objectName);
                        zos.putNextEntry(zipEntry);
                        zos.write(objectData);
                        zos.closeEntry();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al crear el ZIP: " + e.getMessage());
            e.printStackTrace();
        }

        // Crear el objeto ZipFileResponse con los datos del archivo ZIP
        byte[] zipData = baos.toByteArray();
        return new ZipFileResponse("bucket.zip", zipData.length, zipData);
    }

    private byte[] downloadFileFromBucket(String bucket, String objectName){
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
        if(totalSize < 2048){
            return totalSize + " bytes";
        }
        // for any reason, totalsize/1024 returns the double of the real size
        return (totalSize/2048) + " kb";
    }
}
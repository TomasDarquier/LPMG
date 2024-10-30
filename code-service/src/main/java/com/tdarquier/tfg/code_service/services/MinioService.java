package com.tdarquier.tfg.code_service.services;

import com.tdarquier.tfg.code_service.entities.MinioFile;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {

    final
    MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void createNewBucket(String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.makeBucket(
                MakeBucketArgs
                        .builder()
                        .bucket(name)
                        .build()
        );
    }

    public boolean bucketExists(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.bucketExists(
                BucketExistsArgs
                        .builder()
                        .bucket(path)
                        .build()
        );
    }

    public void saveObject(MinioFile minioFile) {
        //transformacion del string del archivo a un stream de bytes
        try (InputStream fileInputStream = new ByteArrayInputStream(minioFile.file().getBytes(StandardCharsets.UTF_8))) {
            // Subir el archivo al bucket
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioFile.path())
                            .object(minioFile.name())
                            .stream(fileInputStream, fileInputStream.available(), -1)
                            .contentType("text/plain")
                            .build()
            );
            System.out.println("El archivo se subió exitosamente a MinIO.");
        } catch (Exception e) {
            System.err.println("Error al subir el archivo: " + e.getMessage());
        }
    }

    // No deberia necesitar acceder a un archivo
//    public byte[] getObject(MinioFile minioFile){
//        try (InputStream stream = minioClient.getObject(GetObjectArgs
//                .builder()
//                .bucket(minioFile.path())
//                .object(minioFile.name())
//                .build())) {
//
//            // Leer el stream y guardar el contenido en memoria
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//
//            while ((bytesRead = stream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//            }
//
//            // Obtener los datos en un arreglo de bytes
//            byte[] fileBytes = byteArrayOutputStream.toByteArray();
//
//            return fileBytes;
//
//            } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}

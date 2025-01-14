package com.tdarquier.tfg.code_service.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MinioServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class MinioServiceImplDiffblueTest {
    @MockBean
    private MinioClient minioClient;

    @Autowired
    private MinioServiceImpl minioServiceImpl;

    /**
     * Test {@link MinioServiceImpl#createNewBucket(String)}.
     * <ul>
     *   <li>Then throw {@link ServerException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#createNewBucket(String)}
     */
    @Test
    @DisplayName("Test createNewBucket(String); then throw ServerException")
    void testCreateNewBucket_thenThrowServerException()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        doThrow(new ServerException("An error occurred", 1, "https://example.org/example")).when(minioClient)
                .makeBucket(Mockito.<MakeBucketArgs>any());

        // Act and Assert
        assertThrows(ServerException.class, () -> minioServiceImpl.createNewBucket("bucket-name"));
        verify(minioClient).makeBucket(isA(MakeBucketArgs.class));
    }

    /**
     * Test {@link MinioServiceImpl#createNewBucket(String)}.
     * <ul>
     *   <li>When {@code bucket-name}.</li>
     *   <li>Then calls {@link MinioClient#makeBucket(MakeBucketArgs)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#createNewBucket(String)}
     */
    @Test
    @DisplayName("Test createNewBucket(String); when 'bucket-name'; then calls makeBucket(MakeBucketArgs)")
    void testCreateNewBucket_whenBucketName_thenCallsMakeBucket()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        doNothing().when(minioClient).makeBucket(Mockito.<MakeBucketArgs>any());

        // Act
        minioServiceImpl.createNewBucket("bucket-name");

        // Assert
        verify(minioClient).makeBucket(isA(MakeBucketArgs.class));
    }

    /**
     * Test {@link MinioServiceImpl#createNewBucket(String)}.
     * <ul>
     *   <li>When {@code lll}.</li>
     *   <li>Then calls {@link MinioClient#makeBucket(MakeBucketArgs)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#createNewBucket(String)}
     */
    @Test
    @DisplayName("Test createNewBucket(String); when 'lll'; then calls makeBucket(MakeBucketArgs)")
    void testCreateNewBucket_whenLll_thenCallsMakeBucket()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        doNothing().when(minioClient).makeBucket(Mockito.<MakeBucketArgs>any());

        // Act
        minioServiceImpl.createNewBucket("lll");

        // Assert
        verify(minioClient).makeBucket(isA(MakeBucketArgs.class));
    }

    /**
     * Test {@link MinioServiceImpl#bucketExists(String)}.
     * <ul>
     *   <li>Given {@link MinioClient}
     * {@link MinioClient#bucketExists(BucketExistsArgs)} return {@code false}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then return {@code false}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#bucketExists(String)}
     */
    @Test
    @DisplayName("Test bucketExists(String); given MinioClient bucketExists(BucketExistsArgs) return 'false'; when 'lll'; then return 'false'")
    void testBucketExists_givenMinioClientBucketExistsReturnFalse_whenLll_thenReturnFalse()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        when(minioClient.bucketExists(Mockito.<BucketExistsArgs>any())).thenReturn(false);

        // Act
        boolean actualBucketExistsResult = minioServiceImpl.bucketExists("lll");

        // Assert
        verify(minioClient).bucketExists(isA(BucketExistsArgs.class));
        assertFalse(actualBucketExistsResult);
    }

    /**
     * Test {@link MinioServiceImpl#bucketExists(String)}.
     * <ul>
     *   <li>Given {@link MinioClient}
     * {@link MinioClient#bucketExists(BucketExistsArgs)} return {@code true}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then return {@code true}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#bucketExists(String)}
     */
    @Test
    @DisplayName("Test bucketExists(String); given MinioClient bucketExists(BucketExistsArgs) return 'true'; when 'lll'; then return 'true'")
    void testBucketExists_givenMinioClientBucketExistsReturnTrue_whenLll_thenReturnTrue()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        when(minioClient.bucketExists(Mockito.<BucketExistsArgs>any())).thenReturn(true);

        // Act
        boolean actualBucketExistsResult = minioServiceImpl.bucketExists("lll");

        // Assert
        verify(minioClient).bucketExists(isA(BucketExistsArgs.class));
        assertTrue(actualBucketExistsResult);
    }

    /**
     * Test {@link MinioServiceImpl#bucketExists(String)}.
     * <ul>
     *   <li>Then throw {@link ServerException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#bucketExists(String)}
     */
    @Test
    @DisplayName("Test bucketExists(String); then throw ServerException")
    void testBucketExists_thenThrowServerException()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        when(minioClient.bucketExists(Mockito.<BucketExistsArgs>any()))
                .thenThrow(new ServerException("An error occurred", 10, "https://example.org/example"));

        // Act and Assert
        assertThrows(ServerException.class, () -> minioServiceImpl.bucketExists("lll"));
        verify(minioClient).bucketExists(isA(BucketExistsArgs.class));
    }
}

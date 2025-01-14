package com.tdarquier.tfg.download_service.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tdarquier.tfg.download_service.dtos.ZipFileResponse;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import io.minio.messages.Contents;
import io.minio.messages.Item;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

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
     * Test {@link MinioServiceImpl#listBucketsById(Long)}.
     * <p>
     * Method under test: {@link MinioServiceImpl#listBucketsById(Long)}
     */
    @Test
    @DisplayName("Test listBucketsById(Long)")
    void testListBucketsById()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        when(minioClient.listBuckets()).thenReturn(new ArrayList<>());

        // Act
        List<String> actualListBucketsByIdResult = minioServiceImpl.listBucketsById(1L);

        // Assert
        verify(minioClient).listBuckets();
        assertTrue(actualListBucketsByIdResult.isEmpty());
    }

    /**
     * Test {@link MinioServiceImpl#listBucketsById(Long)}.
     * <ul>
     *   <li>Given {@link ArrayList#ArrayList()} add {@link Bucket} (default
     * constructor).</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#listBucketsById(Long)}
     */
    @Test
    @DisplayName("Test listBucketsById(Long); given ArrayList() add Bucket (default constructor)")
    void testListBucketsById_givenArrayListAddBucket()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        ArrayList<Bucket> bucketList = new ArrayList<>();
        bucketList.add(new Bucket());
        when(minioClient.listBuckets()).thenReturn(bucketList);

        // Act
        List<String> actualListBucketsByIdResult = minioServiceImpl.listBucketsById(1L);

        // Assert
        verify(minioClient).listBuckets();
        assertTrue(actualListBucketsByIdResult.isEmpty());
    }

    /**
     * Test {@link MinioServiceImpl#listBucketsById(Long)}.
     * <ul>
     *   <li>Given {@link ArrayList#ArrayList()} add {@code null}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#listBucketsById(Long)}
     */
    @Test
    @DisplayName("Test listBucketsById(Long); given ArrayList() add 'null'")
    void testListBucketsById_givenArrayListAddNull()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        ArrayList<Bucket> bucketList = new ArrayList<>();
        bucketList.add(null);
        when(minioClient.listBuckets()).thenReturn(bucketList);

        // Act
        List<String> actualListBucketsByIdResult = minioServiceImpl.listBucketsById(1L);

        // Assert
        verify(minioClient).listBuckets();
        assertTrue(actualListBucketsByIdResult.isEmpty());
    }

    /**
     * Test {@link MinioServiceImpl#listBucketsById(Long)}.
     * <ul>
     *   <li>Given {@link Bucket} {@link Bucket#name()} return
     * {@code bucket-name}.</li>
     *   <li>Then calls {@link Bucket#name()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#listBucketsById(Long)}
     */
    @Test
    @DisplayName("Test listBucketsById(Long); given Bucket name() return 'bucket-name'; then calls name()")
    void testListBucketsById_givenBucketNameReturnBucketName_thenCallsName()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        Bucket bucket = mock(Bucket.class);
        when(bucket.name()).thenReturn("bucket-name");

        ArrayList<Bucket> bucketList = new ArrayList<>();
        bucketList.add(bucket);
        when(minioClient.listBuckets()).thenReturn(bucketList);

        // Act
        List<String> actualListBucketsByIdResult = minioServiceImpl.listBucketsById(1L);

        // Assert
        verify(minioClient).listBuckets();
        verify(bucket).name();
        assertTrue(actualListBucketsByIdResult.isEmpty());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String)")
    void testGetZip()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>(new Contents("lll")));
        Headers headers = (new Headers.Builder()).build();
        when(minioClient.getObject(Mockito.<GetObjectArgs>any()))
                .thenReturn(new GetObjectResponse(headers, "s3://bucket-name/object-key", "us-east-2", "Object", null));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).getObject(isA(GetObjectArgs.class));
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Given {@link ArrayList#ArrayList()} add {@link Result#Result(Exception)}
     * with ex is {@code null}.</li>
     *   <li>When {@code lll}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); given ArrayList() add Result(Exception) with ex is 'null'; when 'lll'")
    void testGetZip_givenArrayListAddResultWithExIsNull_whenLll() {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>((Exception) null));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Given {@link ArrayList#ArrayList()} add {@link Result#Result(Object)}
     * with type is {@link Contents#Contents()}.</li>
     *   <li>When {@code lll}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); given ArrayList() add Result(Object) with type is Contents(); when 'lll'")
    void testGetZip_givenArrayListAddResultWithTypeIsContents_whenLll() {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>(new Contents()));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Given {@link ByteArrayInputStream#ByteArrayInputStream(byte[])} with
     * empty array of {@code byte}.</li>
     *   <li>Then calls {@link MinioClient#getObject(GetObjectArgs)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); given ByteArrayInputStream(byte[]) with empty array of byte; then calls getObject(GetObjectArgs)")
    void testGetZip_givenByteArrayInputStreamWithEmptyArrayOfByte_thenCallsGetObject()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>(new Contents("lll")));
        Headers headers = (new Headers.Builder()).build();
        when(minioClient.getObject(Mockito.<GetObjectArgs>any())).thenReturn(new GetObjectResponse(headers,
                "s3://bucket-name/object-key", "us-east-2", "Object", new ByteArrayInputStream(new byte[]{})));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).getObject(isA(GetObjectArgs.class));
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Given {@link Contents} {@link Item#objectName()} return {@code /}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then calls {@link Item#objectName()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); given Contents objectName() return '/'; when 'lll'; then calls objectName()")
    void testGetZip_givenContentsObjectNameReturnSlash_whenLll_thenCallsObjectName() {
        // Arrange
        Contents contents = mock(Contents.class);
        when(contents.objectName()).thenReturn("/");
        Result<Item> result = new Result<>(contents);

        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(result);
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        verify(contents).objectName();
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Given {@link Contents#Contents(String)} with prefix is empty string.</li>
     *   <li>When {@code lll}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); given Contents(String) with prefix is empty string; when 'lll'")
    void testGetZip_givenContentsWithPrefixIsEmptyString_whenLll() {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>(new Contents("")));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Given {@link Exception#Exception(String)} with {@code lll}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then return FileName is {@code bucket.zip}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); given Exception(String) with 'lll'; when 'lll'; then return FileName is 'bucket.zip'")
    void testGetZip_givenExceptionWithLll_whenLll_thenReturnFileNameIsBucketZip() {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>(new Exception("lll")));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Given {@link GetObjectResponse} {@link InputStream#readAllBytes()} return
     * {@code null}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then calls {@link FilterInputStream#close()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); given GetObjectResponse readAllBytes() return 'null'; when 'lll'; then calls close()")
    void testGetZip_givenGetObjectResponseReadAllBytesReturnNull_whenLll_thenCallsClose()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        Contents contents = mock(Contents.class);
        when(contents.objectName()).thenReturn("Object Name");
        Result<Item> result = new Result<>(contents);

        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(result);
        GetObjectResponse getObjectResponse = mock(GetObjectResponse.class);
        when(getObjectResponse.readAllBytes()).thenReturn(null);
        doNothing().when(getObjectResponse).close();
        when(minioClient.getObject(Mockito.<GetObjectArgs>any())).thenReturn(getObjectResponse);
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).getObject(isA(GetObjectArgs.class));
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        verify(contents).objectName();
        verify(getObjectResponse).close();
        verify(getObjectResponse).readAllBytes();
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Given {@link MinioClient}.</li>
     *   <li>When {@code s3://bucket-name/object-key}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); given MinioClient; when 's3://bucket-name/object-key'")
    void testGetZip_givenMinioClient_whenS3BucketNameObjectKey() {
        // Arrange and Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("s3://bucket-name/object-key");

        // Assert
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Then return array length is one hundred forty-two.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); then return array length is one hundred forty-two")
    void testGetZip_thenReturnArrayLengthIsOneHundredFortyTwo()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        Contents contents = mock(Contents.class);
        when(contents.objectName()).thenReturn("Object Name");
        Result<Item> result = new Result<>(contents);

        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(result);
        GetObjectResponse getObjectResponse = mock(GetObjectResponse.class);
        when(getObjectResponse.readAllBytes()).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        doNothing().when(getObjectResponse).close();
        when(minioClient.getObject(Mockito.<GetObjectArgs>any())).thenReturn(getObjectResponse);
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).getObject(isA(GetObjectArgs.class));
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        verify(contents).objectName();
        verify(getObjectResponse).close();
        verify(getObjectResponse).readAllBytes();
        assertEquals(142, actualZip.getFileData().length);
        assertEquals(142L, actualZip.getFileSize());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>Then return array length is one hundred twenty-six.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); then return array length is one hundred twenty-six")
    void testGetZip_thenReturnArrayLengthIsOneHundredTwentySix()
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>(new Contents("lll")));
        Headers headers = (new Headers.Builder()).build();
        when(minioClient.getObject(Mockito.<GetObjectArgs>any())).thenReturn(new GetObjectResponse(headers,
                "s3://bucket-name/object-key", "us-east-2", "Object", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).getObject(isA(GetObjectArgs.class));
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals(126, actualZip.getFileData().length);
        assertEquals(126L, actualZip.getFileSize());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>When {@code bucket.zip}.</li>
     *   <li>Then return FileName is {@code bucket.zip}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); when 'bucket.zip'; then return FileName is 'bucket.zip'")
    void testGetZip_whenBucketZip_thenReturnFileNameIsBucketZip() {
        // Arrange
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(new ArrayList<>());

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("bucket.zip");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>When {@code lll}.</li>
     *   <li>Then return FileName is {@code bucket.zip}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); when 'lll'; then return FileName is 'bucket.zip'")
    void testGetZip_whenLll_thenReturnFileNameIsBucketZip() {
        // Arrange
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(new ArrayList<>());

        // Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getZip(String)}.
     * <ul>
     *   <li>When {@code /}.</li>
     *   <li>Then return FileName is {@code bucket.zip}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getZip(String)}
     */
    @Test
    @DisplayName("Test getZip(String); when '/'; then return FileName is 'bucket.zip'")
    void testGetZip_whenSlash_thenReturnFileNameIsBucketZip() {
        // Arrange and Act
        ZipFileResponse actualZip = minioServiceImpl.getZip("/");

        // Assert
        assertEquals("bucket.zip", actualZip.getFileName());
        assertEquals(22L, actualZip.getFileSize());
        assertArrayEquals(new byte[]{'P', 'K', 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                actualZip.getFileData());
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link ArrayList#ArrayList()} add {@link Result#Result(Exception)}
     * with ex is {@code null}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then return {@code 0 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given ArrayList() add Result(Exception) with ex is 'null'; when 'lll'; then return '0 bytes'")
    void testGetSize_givenArrayListAddResultWithExIsNull_whenLll_thenReturn0Bytes() {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>((Exception) null));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        String actualSize = minioServiceImpl.getSize("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("0 bytes", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link Contents} {@link Item#size()} return {@code 2048}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then return {@code 1 kb}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given Contents size() return '2048'; when 'lll'; then return '1 kb'")
    void testGetSize_givenContentsSizeReturn2048_whenLll_thenReturn1Kb() {
        // Arrange
        Contents contents = mock(Contents.class);
        when(contents.size()).thenReturn(2048L);
        Result<Item> result = new Result<>(contents);

        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(result);
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        String actualSize = minioServiceImpl.getSize("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        verify(contents).size();
        assertEquals("1 kb", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link Contents} {@link Item#size()} return three.</li>
     *   <li>When {@code lll42}.</li>
     *   <li>Then return {@code 3 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given Contents size() return three; when 'lll42'; then return '3 bytes'")
    void testGetSize_givenContentsSizeReturnThree_whenLll42_thenReturn3Bytes() {
        // Arrange
        Contents contents = mock(Contents.class);
        when(contents.size()).thenReturn(3L);
        Result<Item> result = new Result<>(contents);

        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(result);
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        String actualSize = minioServiceImpl.getSize("lll42");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        verify(contents).size();
        assertEquals("3 bytes", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link Contents} {@link Item#size()} return three.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then return {@code 3 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given Contents size() return three; when 'lll'; then return '3 bytes'")
    void testGetSize_givenContentsSizeReturnThree_whenLll_thenReturn3Bytes() {
        // Arrange
        Contents contents = mock(Contents.class);
        when(contents.size()).thenReturn(3L);
        Result<Item> result = new Result<>(contents);

        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(result);
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        String actualSize = minioServiceImpl.getSize("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        verify(contents).size();
        assertEquals("3 bytes", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link Contents} {@link Item#size()} return three.</li>
     *   <li>When {@code lllbucket.zip}.</li>
     *   <li>Then return {@code 3 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given Contents size() return three; when 'lllbucket.zip'; then return '3 bytes'")
    void testGetSize_givenContentsSizeReturnThree_whenLllbucketZip_thenReturn3Bytes() {
        // Arrange
        Contents contents = mock(Contents.class);
        when(contents.size()).thenReturn(3L);
        Result<Item> result = new Result<>(contents);

        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(result);
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        String actualSize = minioServiceImpl.getSize("lllbucket.zip");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        verify(contents).size();
        assertEquals("3 bytes", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link Contents} {@link Item#size()} return three.</li>
     *   <li>When {@code llllll}.</li>
     *   <li>Then return {@code 3 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given Contents size() return three; when 'llllll'; then return '3 bytes'")
    void testGetSize_givenContentsSizeReturnThree_whenLlllll_thenReturn3Bytes() {
        // Arrange
        Contents contents = mock(Contents.class);
        when(contents.size()).thenReturn(3L);
        Result<Item> result = new Result<>(contents);

        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(result);
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        String actualSize = minioServiceImpl.getSize("llllll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        verify(contents).size();
        assertEquals("3 bytes", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link Contents#Contents(String)} with prefix is {@code lll}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then return {@code 0 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given Contents(String) with prefix is 'lll'; when 'lll'; then return '0 bytes'")
    void testGetSize_givenContentsWithPrefixIsLll_whenLll_thenReturn0Bytes() {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>(new Contents("lll")));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        String actualSize = minioServiceImpl.getSize("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("0 bytes", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link Exception#Exception(String)} with {@code lll}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then return {@code 0 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given Exception(String) with 'lll'; when 'lll'; then return '0 bytes'")
    void testGetSize_givenExceptionWithLll_whenLll_thenReturn0Bytes() {
        // Arrange
        ArrayList<Result<Item>> resultList = new ArrayList<>();
        resultList.add(new Result<>(new Exception("lll")));
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(resultList);

        // Act
        String actualSize = minioServiceImpl.getSize("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("0 bytes", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link MinioClient}
     * {@link MinioClient#listObjects(ListObjectsArgs)} return
     * {@link ArrayList#ArrayList()}.</li>
     *   <li>When {@code lll}.</li>
     *   <li>Then return {@code 0 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given MinioClient listObjects(ListObjectsArgs) return ArrayList(); when 'lll'; then return '0 bytes'")
    void testGetSize_givenMinioClientListObjectsReturnArrayList_whenLll_thenReturn0Bytes() {
        // Arrange
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(new ArrayList<>());

        // Act
        String actualSize = minioServiceImpl.getSize("lll");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("0 bytes", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>Given {@link MinioClient}.</li>
     *   <li>When {@code s3://bucket-name/object-key}.</li>
     *   <li>Then return {@code 0 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); given MinioClient; when 's3://bucket-name/object-key'; then return '0 bytes'")
    void testGetSize_givenMinioClient_whenS3BucketNameObjectKey_thenReturn0Bytes() {
        // Arrange, Act and Assert
        assertEquals("0 bytes", minioServiceImpl.getSize("s3://bucket-name/object-key"));
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>When {@code bucket.zip}.</li>
     *   <li>Then return {@code 0 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); when 'bucket.zip'; then return '0 bytes'")
    void testGetSize_whenBucketZip_thenReturn0Bytes() {
        // Arrange
        when(minioClient.listObjects(Mockito.<ListObjectsArgs>any())).thenReturn(new ArrayList<>());

        // Act
        String actualSize = minioServiceImpl.getSize("bucket.zip");

        // Assert
        verify(minioClient).listObjects(isA(ListObjectsArgs.class));
        assertEquals("0 bytes", actualSize);
    }

    /**
     * Test {@link MinioServiceImpl#getSize(String)}.
     * <ul>
     *   <li>When {@code /}.</li>
     *   <li>Then return {@code 0 bytes}.</li>
     * </ul>
     * <p>
     * Method under test: {@link MinioServiceImpl#getSize(String)}
     */
    @Test
    @DisplayName("Test getSize(String); when '/'; then return '0 bytes'")
    void testGetSize_whenSlash_thenReturn0Bytes() {
        // Arrange, Act and Assert
        assertEquals("0 bytes", minioServiceImpl.getSize("/"));
    }
}

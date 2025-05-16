package com.epam.edp.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;




public class S3ServiceTest {

    private S3Client s3ClientMock;
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        s3ClientMock = Mockito.mock(S3Client.class);
        s3Service = new S3Service(s3ClientMock);
    }

    @Test
    void testGetS3ContentSuccess() {
        // Mock S3Client behavior
        String mockContent = "This is a test file content.";
        InputStream mockInputStream = new ByteArrayInputStream(mockContent.getBytes());
        ResponseInputStream<GetObjectResponse> mockResponseInputStream = 
            new ResponseInputStream<>(GetObjectResponse.builder().build(), mockInputStream);
        Mockito.when(s3ClientMock.getObject(any(GetObjectRequest.class))).thenReturn(mockResponseInputStream);

        // Call the method
        Map<String, String> result = s3Service.getS3Content();

        // Verify the result
        assertNotNull(result);
        assertEquals(mockContent, result.get("content"));
    }

    @Test
    void testGetS3ContentFailure() {
        // Mock S3Client to throw an exception
        Mockito.when(s3ClientMock.getObject(any(GetObjectRequest.class))).thenThrow(S3Exception.builder().message("S3 error").build());

        // Verify that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> s3Service.getS3Content());
        assertEquals("Failed to fetch file from S3", exception.getMessage());
    }
}
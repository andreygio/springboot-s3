package com.epam.edp.demo.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final S3Client s3Client;

    // Constructor-based injection
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public Map<String, String> getS3Content() {
        String bucketName = "kuberocketci-applications-data";
        String key = "cmtr-dsdcocl2/data.txt";

        try {
            // Build the GetObjectRequest without requiring ListBucket permission
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            // Fetch the file from S3 (GetObject does not require ListBucket)
            InputStream inputStream = s3Client.getObject(getObjectRequest);

            // Read the content of the file
            String content = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

            // Prepare the JSON response
            Map<String, String> response = new HashMap<>();
            response.put("content", content);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch file from S3", e);
        }
    }
}
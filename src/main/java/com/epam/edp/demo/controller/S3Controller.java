package com.epam.edp.demo.controller;

// Ensure the correct package path for S3Service
import com.epam.edp.demo.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * S3Controller to handle S3 file operations.
 */
@RestController
public class S3Controller {

    private final S3Service s3Service; // Inject S3Service instead of S3Client

    @Autowired
    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping(value = "/")
    public Map<String, String> getS3FileContent() {
        // Use the S3Service to fetch the file content
        return s3Service.getS3Content();
    }
}

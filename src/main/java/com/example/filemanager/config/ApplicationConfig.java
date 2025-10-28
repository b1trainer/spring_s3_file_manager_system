package com.example.filemanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    private ApplicationConfig() {
    }

    @Value("${aws.s3.bucket.name}")
    private static String bucketName;

    public static String getBucketName() {
        return bucketName;
    }
}

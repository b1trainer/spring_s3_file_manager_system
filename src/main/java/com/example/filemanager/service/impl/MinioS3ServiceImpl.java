package com.example.filemanager.service.impl;

import com.example.filemanager.config.S3Config;
import com.example.filemanager.service.MinioS3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
public class MinioS3ServiceImpl implements MinioS3Service {

    private final S3Client s3Client;

    public MinioS3ServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public Mono<ResponseBytes<GetObjectResponse>> getObjectFromS3(String location) {
        return Mono.fromCallable(() -> s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .key(location)
                        .bucket(S3Config.getBucket())
                        .build())
        );
    }

    public Mono<PutObjectResponse> putObjectToS3(String location, MultipartFile file) {
        return Mono.fromCallable(() ->
                s3Client.putObject(PutObjectRequest.builder()
                                .key(location)
                                .bucket(S3Config.getBucket())
                                .contentType(file.getContentType())
                                .build(),
                        RequestBody.fromBytes(file.getBytes()))
        );
    }

    public Mono<DeleteObjectResponse> deleteObjectFromS3(String location) {
        return Mono.fromCallable(() ->
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .key(location)
                        .bucket(S3Config.getBucket())
                        .build())
        );
    }
}

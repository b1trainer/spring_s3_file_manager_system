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

import java.sql.SQLException;

@Service
public class MinioS3ServiceImpl implements MinioS3Service {

    private final S3Client s3Client;
    private final S3Config s3Config;

    public MinioS3ServiceImpl(S3Client s3Client, S3Config s3Config) {
        this.s3Client = s3Client;
        this.s3Config = s3Config;

        String bucketName = s3Config.getBucket();

        try {
            s3Client.headBucket(builder -> builder.bucket(bucketName));
        } catch (S3Exception e) {
            s3Client.createBucket(builder -> builder.bucket(bucketName));
        }
    }

    public Mono<ResponseBytes<GetObjectResponse>> getObjectFromS3(String location) {
        return Mono.fromCallable(() -> s3Client.getObjectAsBytes(
                        GetObjectRequest.builder()
                                .bucket(s3Config.getBucket())
                                .key(location)
                                .build())
                )
                .onErrorMap(Exception.class, e -> S3Exception.builder().message("Ошибка при получении файла из хранилища S3: " + e).build());
    }

    public Mono<PutObjectResponse> putObjectToS3(String location, MultipartFile file) {
        return Mono.fromCallable(() ->
                        s3Client.putObject(PutObjectRequest.builder()
                                        .bucket(s3Config.getBucket())
                                        .key(location)
                                        .contentType(file.getContentType())
                                        .build(),
                                RequestBody.fromBytes(file.getBytes()))
                )
                .onErrorMap(Exception.class, e -> S3Exception.builder().message("Ошибка при сохранении файла в хранилище S3: " + e).build());
    }

    public Mono<DeleteObjectResponse> deleteObjectFromS3(String location) {
        return Mono.fromCallable(() ->
                        s3Client.deleteObject(DeleteObjectRequest.builder()
                                .bucket(s3Config.getBucket())
                                .key(location)
                                .build())
                )
                .onErrorMap(Exception.class, e -> S3Exception.builder().message("Ошибка при удалении файла из хранилища S3: " + e).build());
    }
}

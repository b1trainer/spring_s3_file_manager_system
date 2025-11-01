package com.example.filemanager.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public interface MinioS3Service {
    Mono<ResponseBytes<GetObjectResponse>> getObjectFromS3(String location);

    Mono<PutObjectResponse> putObjectToS3(String location, MultipartFile file);

    Mono<DeleteObjectResponse> deleteObjectFromS3(String location);
}

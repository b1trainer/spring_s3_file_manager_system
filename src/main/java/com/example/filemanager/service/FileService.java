package com.example.filemanager.service;

import com.example.filemanager.config.status.FileStatus;
import com.example.filemanager.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

public interface FileService {
    Mono<ResponseBytes<GetObjectResponse>> getFile(String location);

    Mono<FileDTO> loadFile(MultipartFile file, Long userId) throws IOException;

    Mono<Void> updateFileStatus(String fileId, FileStatus status);

    Mono<Void> deleteFile(String location);
}

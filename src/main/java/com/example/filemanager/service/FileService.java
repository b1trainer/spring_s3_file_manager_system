package com.example.filemanager.service;

import com.example.filemanager.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

public interface FileService {
    public ResponseBytes<GetObjectResponse> getFile(String location);

    public Mono<FileDTO> createFile(MultipartFile file) throws IOException;

    public Mono<FileDTO> updateFile(FileDTO fileDTO);

    public Mono<Void> deleteFile(String location);
}

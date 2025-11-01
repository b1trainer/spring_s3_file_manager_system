package com.example.filemanager.service;

import com.example.filemanager.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

public interface FileService {
    Mono<ResponseBytes<GetObjectResponse>> getFile(String location);

    Mono<FileDTO> loadFile(MultipartFile file) throws IOException;

    Mono<FileDTO> updateFile(String fileId, FileDTO fileDTO);

    Mono<Void> deleteFile(String location);
}

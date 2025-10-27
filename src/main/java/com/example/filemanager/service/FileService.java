package com.example.filemanager.service;

import com.example.filemanager.dto.FileDTO;
import reactor.core.publisher.Mono;

public interface FileService {
    public Mono<FileDTO> getFile(String location);

    public Mono<FileDTO> createFile(FileDTO fileDTO);

    public Mono<FileDTO> updateFile(FileDTO fileDTO);

    public Mono<Void> deleteFile(String location);
}

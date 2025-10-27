package com.example.filemanager.service.impl;

import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.service.FileService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public Mono<FileDTO> getFile(String location) {
        return null;
    }

    @Override
    public Mono<FileDTO> createFile(FileDTO fileDTO) {
        return null;
    }

    @Override
    public Mono<FileDTO> updateFile(FileDTO fileDTO) {
        return null;
    }

    @Override
    public Mono<Void> deleteFile(String location) {
        return null;
    }
}

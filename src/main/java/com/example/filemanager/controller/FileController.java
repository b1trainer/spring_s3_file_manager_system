package com.example.filemanager.controller;

import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public Mono<ResponseEntity<FileDTO>> uploadFile() {
    }

    @GetMapping
    public Mono<ResponseEntity<FileDTO>> getFile() {
    }

    @PutMapping
    public Mono<ResponseEntity<FileDTO>> updateFile() {
    }

    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteFile() {}
}

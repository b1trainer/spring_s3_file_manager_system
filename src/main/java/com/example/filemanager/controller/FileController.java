package com.example.filemanager.controller;

import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/rest/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public Mono<ResponseEntity<FileDTO>> uploadFile(@RequestPart(name = "file") MultipartFile file) throws IOException {
        return fileService.loadFile(file)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{fileId}")
    public Mono<ResponseEntity<byte[]>> getFile(@PathVariable String fileId) {
        return fileService.getFile(fileId).map(file ->
                ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(file.response().contentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, file.response().contentDisposition())
                        .body(file.asByteArray())
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{fileId}")
    public Mono<ResponseEntity<FileDTO>> updateFile(@PathVariable String fileId, @RequestBody FileDTO fileDTO) {
        return fileService.updateFile(fileId, fileDTO)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{fileId}")
    public Mono<ResponseEntity<Void>> deleteFile(@PathVariable String fileId) {
        return fileService.deleteFile(fileId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}

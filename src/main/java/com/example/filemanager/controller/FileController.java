package com.example.filemanager.controller;

import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.security.CustomPrincipal;
import com.example.filemanager.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;

@RestController
@RequestMapping("/rest/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<FileDTO>> uploadFile(@AuthenticationPrincipal Object principal,
                                                    @RequestPart(name = "file") MultipartFile file) throws IOException {
        CustomPrincipal customPrincipal = (CustomPrincipal) principal;
        return fileService.loadFile(file, customPrincipal.getId())
                .onErrorMap(Exception.class, e -> new ServiceUnavailableException("Ошибка загрузки файла: " + e))
                .map(fileDTO -> ResponseEntity.status(HttpStatus.CREATED).body(fileDTO));
    }

    @GetMapping("/{fileId}")
    public Mono<ResponseEntity<byte[]>> getFile(@PathVariable Long fileId) {
        return fileService.getFile(fileId)
                .onErrorMap(Exception.class, e -> new ServiceUnavailableException("Ошибка получения файла: " + e))
                .map(file ->
                        ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(file.response().contentType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION, file.response().contentDisposition())
                                .body(file.asByteArray())
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{fileId}/{status}")
    public Mono<ResponseEntity<FileDTO>> updateFileStatus(@PathVariable Long fileId, @PathVariable FileDTO.FileStatus status) {
        return fileService.updateFileStatus(fileId, status)
                .onErrorMap(Exception.class, e -> new ServiceUnavailableException("Ошибка обновления статуса файла: " + e))
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/{fileId}")
    public Mono<ResponseEntity<Void>> deleteFile(@PathVariable Long fileId) {
        return fileService.deleteFile(fileId)
                .onErrorMap(Exception.class, e -> new ServiceUnavailableException("Ошибка удаления файла: " + e))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}

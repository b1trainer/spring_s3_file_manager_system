package com.example.filemanager.controller;

import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequestMapping("/rest/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public Mono<ResponseEntity<FileDTO>> uploadFile(@RequestPart(name = "file") MultipartFile file) {
        return fileService.createFile(file)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{fileId}")
    public Mono<ResponseEntity<byte[]>> getFile(@PathVariable String fileId) {
        ResponseBytes<GetObjectResponse> objectBytes = fileService.getFile(fileId);

        String contentType = objectBytes.response().contentType();
        String filename = objectBytes.response().metadata().get("filename");

        ResponseEntity responseEntity = ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(objectBytes.asByteArray());

        return Mono.just(responseEntity);
    }

    @PutMapping("/{fileId}")
    public Mono<ResponseEntity<FileDTO>> updateFile(@PathVariable String fileId, @RequestBody FileDTO fileDTO) {
        return fileService.updateFile(fileDTO)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{fileId}")
    public Mono<ResponseEntity<Void>> deleteFile(@PathVariable String fileId) {
        return fileService.deleteFile(fileId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}

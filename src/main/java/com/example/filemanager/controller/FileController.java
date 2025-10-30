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
    } // @RequestBody fileDto, @RequestPart(name = "file") MultipartFile file ??

    @GetMapping("/{fileId}")
    public Mono<Object> getFile(@PathVariable String fileId) {

        FileDTO fileDTO = fileService.getFile(fileId);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDTO.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileDTO.getName() + "\"")
                .body(fileDTO.getContent()));
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

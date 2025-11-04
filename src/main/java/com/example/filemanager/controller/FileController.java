package com.example.filemanager.controller;

import com.example.filemanager.config.status.FileStatus;
import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.security.CustomPrincipal;
import com.example.filemanager.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public Mono<ResponseEntity<FileDTO>> uploadFile(@AuthenticationPrincipal Authentication auth,
                                                    @RequestPart(name = "file") MultipartFile file) throws IOException {
        CustomPrincipal principal = (CustomPrincipal) auth.getPrincipal();
        return fileService.loadFile(file, principal.getId())
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }

    @Secured("{ADMIN, MODERATOR}")
    @GetMapping
    public Mono<ResponseEntity<List<FileDTO>>> getListOfAllUserFiles(
            @RequestParam("userId") String userId) {
        Long id = Long.parseLong(userId);
        return fileService.findFilesForUser(id).map(files -> ResponseEntity.ok().body(files));
    }

    @GetMapping
    public Mono<ResponseEntity<List<FileDTO>>> getListOfFiles(@AuthenticationPrincipal Authentication auth) {
        CustomPrincipal principal = (CustomPrincipal) auth.getPrincipal();
        return fileService.findFilesForUser(principal.getId()).map(files -> ResponseEntity.ok().body(files));
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

    @PatchMapping("/{fileId}/{status}")
    public Mono<ResponseEntity<FileDTO>> updateFileStatus(@PathVariable String fileId, @PathVariable FileStatus status) {
        return fileService.updateFileStatus(fileId, status)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/{fileId}")
    public Mono<ResponseEntity<Void>> deleteFile(@PathVariable String fileId) {
        return fileService.deleteFile(fileId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}

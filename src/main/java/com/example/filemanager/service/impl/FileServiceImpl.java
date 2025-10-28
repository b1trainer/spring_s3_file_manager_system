package com.example.filemanager.service.impl;

import com.example.filemanager.config.ApplicationConfig;
import com.example.filemanager.config.status.FileStatus;
import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.repository.FileRepository;
import com.example.filemanager.service.FileService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final S3Client s3Client;

    public FileServiceImpl(FileRepository fileRepository, S3Client s3Client) {
        this.fileRepository = fileRepository;
        this.s3Client = s3Client;
    }

    @Override
    public ResponseBytes<GetObjectResponse> getFile(String fileId) {
        String location = fileRepository.getFileLocation(fileId);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .key(location)
                .bucket(ApplicationConfig.getBucketName())
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);

        return objectBytes;
    }

    @Override
    @Transactional
    public Mono<FileDTO> createFile(MultipartFile file) throws IOException {
        String location = ApplicationConfig.getBucketName() + "/" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(location)
                .bucket(ApplicationConfig.getBucketName())
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        FileDTO fileDTO = new FileDTO();
        fileDTO.setName(file.getName());
        fileDTO.setStatus(FileStatus.ACTIVE);
        fileDTO.setLocation(location);

        fileRepository.saveFileInfo(fileDTO); // change repo

        return Mono.just(fileDTO);
    }

    @Override
    @Transactional
    public Mono<FileDTO> updateFile(FileDTO fileDTO) {
        return null;
    }

    @Override
    @Transactional
    public Mono<Void> deleteFile(String fileId) {
        return null;
    }

    private String generateDocumentKey(String fileId, String bucket) {
        return String.format("%s_%s", bucket, fileId);
    }
}

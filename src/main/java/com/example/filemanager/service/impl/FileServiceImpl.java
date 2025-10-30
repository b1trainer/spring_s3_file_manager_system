package com.example.filemanager.service.impl;

import com.example.filemanager.config.ApplicationConfig;
import com.example.filemanager.config.status.FileStatus;
import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.mapper.FileMapper;
import com.example.filemanager.repository.EventRepository;
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
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final EventRepository eventRepository;
    private final S3Client s3Client;
    private final FileMapper fileMapper;

    public FileServiceImpl(FileRepository fileRepository, EventRepository eventRepository, S3Client s3Client, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.eventRepository = eventRepository;
        this.s3Client = s3Client;
        this.fileMapper = fileMapper;
    }

    @Override
    public FileDTO getFile(String fileId) {
        Long id = Long.parseLong(fileId);
        FileEntity fileEntity = fileRepository.getFileEntityById(id);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .key(fileEntity.getLocation())
                .bucket(ApplicationConfig.getBucketName())
                .build();

        ResponseBytes<GetObjectResponse> s3response = s3Client.getObjectAsBytes(getObjectRequest);

        FileDTO fileDTO = fileMapper.map(fileEntity); // n+1 in mapper?
        fileDTO.setContent(s3response.asByteArray());
        fileDTO.setContentType(s3response.response().contentType());

        return fileDTO;
    }

    @Override
    @Transactional
    public Mono<FileDTO> loadFile(FileDTO fileDto) {
        String location = ApplicationConfig.getBucketName() + "/" + fileDto.getName();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(location)
                .bucket(ApplicationConfig.getBucketName())
                .contentType(fileDto.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileDto.getContent()));

        FileEntity fileEntity = fileMapper.map(fileDto);
        fileEntity.setLocation(location);

        fileRepository.save(fileEntity);

        return Mono.just(fileDto); // ??? dont return anything?
    }

    @Override
    public Mono<FileDTO> updateFile(String fileId, FileDTO fileDTO) {
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

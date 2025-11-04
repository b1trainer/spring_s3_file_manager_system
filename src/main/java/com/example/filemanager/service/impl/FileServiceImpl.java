package com.example.filemanager.service.impl;

import com.example.filemanager.config.S3Config;
import com.example.filemanager.config.status.EventStatus;
import com.example.filemanager.config.status.FileStatus;
import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.entity.EventEntity;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.mapper.FileMapper;
import com.example.filemanager.repository.EventRepository;
import com.example.filemanager.repository.FileRepository;
import com.example.filemanager.service.FileService;
import com.example.filemanager.service.MinioS3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.time.Instant;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final EventRepository eventRepository;
    private final MinioS3Service minioS3Service;
    private final FileMapper fileMapper;
    private final S3Config s3Config;

    public FileServiceImpl(FileRepository fileRepository, EventRepository eventRepository, MinioS3Service minioS3Service, FileMapper fileMapper, S3Config s3Config) {
        this.fileRepository = fileRepository;
        this.eventRepository = eventRepository;
        this.minioS3Service = minioS3Service;
        this.fileMapper = fileMapper;
        this.s3Config = s3Config;
    }

    @Override
    public Mono<ResponseBytes<GetObjectResponse>> getFile(String fileId) {
        Long id = Long.parseLong(fileId);

        return fileRepository.findById(id)
                .flatMap(fileEntity -> minioS3Service.getObjectFromS3(fileEntity.getLocation()));
    }

    @Override
    @Transactional
    public Mono<FileDTO> loadFile(MultipartFile file, Long userId) {
        String location = s3Config.getBucket() + "/" + file.getOriginalFilename();

        FileDTO fileDTO = new FileDTO();
        fileDTO.setLocation(location);
        fileDTO.setName(file.getOriginalFilename());
        fileDTO.setStatus(FileStatus.ACTIVE);
        fileDTO.setUserId(userId);

        return minioS3Service.putObjectToS3(location, file)
                .flatMap(res -> fileRepository.save(fileMapper.map(fileDTO)))
                .flatMap(fileEntity -> {
                    EventEntity event = createEvent(fileEntity, EventStatus.CREATED);

                    return eventRepository.save(event);
                })
                .thenReturn(fileDTO);
    }

    @Override
    @Transactional
    public Mono<Void> updateFileStatus(String fileId, FileStatus status) {
        Long id = Long.parseLong(fileId);

        return fileRepository.findById(id)
                .flatMap(fileEntity -> {
                    fileEntity.setStatus(status);
                    return Mono.empty();
                });
    }

    @Override
    @Transactional
    public Mono<Void> deleteFile(String fileId) {
        Long id = Long.parseLong(fileId);

        return fileRepository.findById(id)
                .flatMap(fileEntity ->
                        minioS3Service.deleteObjectFromS3(fileEntity.getLocation())
                                .thenReturn(fileEntity)
                )
                .flatMap(fileEntity ->
                        fileRepository.delete(fileEntity)
                                .then(eventRepository.save(createEvent(null, EventStatus.DELETED)))
                )
                .then();
    }

    private EventEntity createEvent(FileEntity fileEntity, EventStatus eventStatus) {
        EventEntity event = new EventEntity();
        if (fileEntity != null) {
            event.setFileId(fileEntity.getId());
            event.setUserId(fileEntity.getUserId());
        }
        event.setStatus(eventStatus);
        event.setTimestamp(Instant.now());
        return event;
    }
}

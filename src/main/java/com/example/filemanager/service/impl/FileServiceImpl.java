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

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

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
    public Mono<ResponseBytes<GetObjectResponse>> getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .onErrorMap(RuntimeException.class, e -> new SQLException("Ошибка при поиске файла в базе данных: " + e))
                .flatMap(fileEntity -> minioS3Service.getObjectFromS3(fileEntity.getLocation()));
    }

    @Override
    public Mono<List<FileDTO>> findFilesForUser(Long userId) {
        return fileRepository.findAllByUserId(userId)
                .collectList()
                .map(fileEntities -> fileEntities.stream()
                        .map(fileMapper::map)
                        .toList())
                .onErrorMap(RuntimeException.class, e -> new SQLException("Ошибка при поиске файла в базе данных: " + e));
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
                .onErrorMap(RuntimeException.class, e -> new SQLException("Ошибка при сохранении файла в хранилище S3: " + e))
                .flatMap(res -> fileRepository.save(fileMapper.map(fileDTO)))
                .onErrorMap(RuntimeException.class, e -> new SQLException("Ошибка при сохранении файла в базе данных: " + e))
                .flatMap(fileEntity -> {
                    EventEntity event = createEvent(fileEntity, EventStatus.CREATED);

                    return eventRepository.save(event);
                })
                .onErrorContinue((e, obj) -> new SQLException("Ошибка при сохранении события в базе данных: " + e))
                .thenReturn(fileDTO);
    }

    @Override
    @Transactional
    public Mono<Void> updateFileStatus(Long fileId, FileStatus status) {
        return fileRepository.findById(fileId)
                .onErrorMap(RuntimeException.class, e -> new SQLException("Ошибка при поиске файла в базе данных: " + e))
                .flatMap(fileEntity -> {
                    fileEntity.setStatus(status);
                    return Mono.empty();
                });
    }

    @Override
    @Transactional
    public Mono<Void> deleteFile(Long fileId) {
        return fileRepository.findById(fileId)
                .onErrorMap(RuntimeException.class, e -> new SQLException("Ошибка при поиске файла в базе данных: " + e))
                .flatMap(fileEntity ->
                        minioS3Service.deleteObjectFromS3(fileEntity.getLocation())
                                .thenReturn(fileEntity)
                )
                .onErrorMap(RuntimeException.class, e -> new SQLException("Ошибка при удалении файла из хранилища S3: " + e))
                .flatMap(fileEntity ->
                        fileRepository.delete(fileEntity)
                                .onErrorMap(RuntimeException.class, e -> new SQLException("Ошибка при удалении файла из базы данных: " + e))
                                .then(eventRepository.save(createEvent(null, EventStatus.DELETED)))
                                .onErrorMap(RuntimeException.class, e -> new SQLException("Ошибка сохранении события в базу данных: " + e))
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
        return event;
    }
}

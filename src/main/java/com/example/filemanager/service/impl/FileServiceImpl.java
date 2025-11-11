package com.example.filemanager.service.impl;

import com.example.filemanager.config.S3Config;
import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.entity.EventEntity;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.exceptions.DbException;
import com.example.filemanager.mapper.FileMapper;
import com.example.filemanager.repository.EventRepository;
import com.example.filemanager.repository.FileRepository;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.service.FileService;
import com.example.filemanager.service.MinioS3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.sql.SQLException;

@Service
public class FileServiceImpl implements FileService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final EventRepository eventRepository;
    private final MinioS3Service minioS3Service;
    private final FileMapper fileMapper;
    private final S3Config s3Config;

    public FileServiceImpl(UserRepository userRepository, FileRepository fileRepository, EventRepository eventRepository, MinioS3Service minioS3Service, FileMapper fileMapper, S3Config s3Config) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.eventRepository = eventRepository;
        this.minioS3Service = minioS3Service;
        this.fileMapper = fileMapper;
        this.s3Config = s3Config;
    }

    @Override
    public Mono<ResponseBytes<GetObjectResponse>> getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .onErrorMap(Exception.class, e -> new DbException("Ошибка при поиске файла в базе данных: " + e))
                .flatMap(fileEntity -> minioS3Service.getObjectFromS3(fileEntity.getLocation()));
    }

    @Override
    @Transactional
    public Mono<FileDTO> loadFile(MultipartFile file, Long userId) {
        String location = s3Config.getBucket() + "/" + file.getOriginalFilename();

        return userRepository.findById(userId)
                .onErrorMap(Exception.class, e -> new DbException("Ошибка поиска пользователя при сохранении файла: " + e))
                .flatMap(userEntity ->
                        minioS3Service.putObjectToS3(location, file)
                                .flatMap(res -> {
                                    FileDTO fileDTO = new FileDTO();
                                    fileDTO.setLocation(location);
                                    fileDTO.setName(file.getOriginalFilename());
                                    fileDTO.setStatus(FileDTO.FileStatus.ACTIVE);

                                    return fileRepository.save(fileMapper.map(fileDTO))
                                            .onErrorMap(Exception.class, e -> new DbException("Ошибка при сохранении файла в базе данных: " + e))
                                            .flatMap(fileEntity ->
                                                    eventRepository.save(createEvent(fileEntity, userEntity, EventDTO.EventStatus.CREATED))
                                                            .onErrorMap(Exception.class, e -> new DbException("Ошибка при сохранении события в базе данных: " + e))
                                                            .thenReturn(fileMapper.map(fileEntity))
                                            );
                                })
                );
    }

    @Override
    @Transactional
    public Mono<Void> updateFileStatus(Long fileId, FileDTO.FileStatus status) {
        return fileRepository.findById(fileId)
                .onErrorMap(Exception.class, e -> new DbException("Ошибка при поиске файла в базе данных: " + e))
                .flatMap(fileEntity -> {
                    fileEntity.setStatus(status);
                    return Mono.empty();
                });
    }

    @Override
    @Transactional
    public Mono<Void> deleteFile(Long fileId) {
        return fileRepository.findById(fileId)
                .onErrorMap(Exception.class, e -> new DbException("Ошибка при поиске файла в базе данных: " + e))
                .flatMap(fileEntity ->
                        minioS3Service.deleteObjectFromS3(fileEntity.getLocation())
                                .thenReturn(fileEntity)
                )
                .flatMap(fileEntity ->
                        fileRepository.delete(fileEntity)
                                .onErrorMap(Exception.class, e -> new SQLException("Ошибка при удалении файла из базы данных: " + e))
                                .then(eventRepository
                                        .save(createEvent(null, null, EventDTO.EventStatus.DELETED))
                                        .onErrorMap(Exception.class, e -> new SQLException("Ошибка сохранении события в базу данных: " + e)))
                )
                .then();
    }

    private EventEntity createEvent(FileEntity fileEntity, UserEntity userEntity, EventDTO.EventStatus eventStatus) {
        EventEntity event = new EventEntity();
        event.setStatus(eventStatus);
        event.setFile(fileEntity);
        event.setUser(userEntity);

        return event;
    }
}

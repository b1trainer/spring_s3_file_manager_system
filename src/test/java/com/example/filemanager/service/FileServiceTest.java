package com.example.filemanager.service;

import com.example.filemanager.config.S3Config;
import com.example.filemanager.config.status.FileStatus;
import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.entity.EventEntity;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.mapper.FileMapper;
import com.example.filemanager.repository.EventRepository;
import com.example.filemanager.repository.FileRepository;
import com.example.filemanager.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    private static FileEntity fileEntity;
    private static FileDTO fileDTO;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private MinioS3Service minioS3Service;

    @Mock
    private FileMapper fileMapper;

    @Mock
    private S3Config s3Config;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeAll
    static void setUp() {
        fileEntity = new FileEntity();
        fileEntity.setId(1L);
        fileEntity.setName("test.txt");
        fileEntity.setStatus(FileStatus.ACTIVE);
        fileEntity.setLocation("bucket/test.txt");
        fileEntity.setUserId(123L);

        fileDTO = new FileDTO();
        fileDTO.setLocation("bucket/test.txt");
        fileDTO.setUserId(123L);
        fileDTO.setName("test.txt");
        fileDTO.setStatus(FileStatus.ACTIVE);
    }

    @Test
    void getFile() {
        ResponseBytes<GetObjectResponse> responseBytes = mock(ResponseBytes.class);

        when(fileRepository.findById(anyLong())).thenReturn(Mono.just(fileEntity));
        when(minioS3Service.getObjectFromS3(fileEntity.getLocation())).thenReturn(Mono.just(responseBytes));

        StepVerifier.create(fileService.getFile(123L))
                .expectNext(responseBytes)
                .verifyComplete();

        verify(fileRepository, times(1)).findById(anyLong());
        verify(minioS3Service, times(1)).getObjectFromS3(fileEntity.getLocation());
    }

    @Test
    void findFilesForUser() {
        List<FileEntity> files = new ArrayList<>();
        files.add(fileEntity);

        when(fileRepository.findAllByUserId(anyLong())).thenReturn(Flux.fromIterable(files));
        when(fileMapper.map(any(FileEntity.class))).thenReturn(fileDTO);

        StepVerifier.create(fileService.findFilesForUser(123L))
                .expectNextMatches(list -> list.contains(fileDTO))
                .verifyComplete();

        verify(fileRepository, times(1)).findAllByUserId(anyLong());
        verify(fileMapper, times(files.size())).map(any(FileEntity.class));
    }

    @Test
    void loadFile() {
        PutObjectResponse response = mock(PutObjectResponse.class);
        String bucket = "test/location";

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test file content".getBytes()
        );

        when(s3Config.getBucket()).thenReturn(bucket);
        when(minioS3Service.putObjectToS3(anyString(), eq(multipartFile))).thenReturn(Mono.just(response));
        when(fileMapper.map(any(FileDTO.class))).thenReturn(fileEntity);
        when(fileRepository.save(fileEntity)).thenReturn(Mono.just(fileEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(Mono.just(new EventEntity()));

        StepVerifier.create(fileService.loadFile(multipartFile, 123L))
                .expectNextMatches(file -> file.getName().equals("test.txt")
                        && file.getUserId() == 123L
                        && file.getStatus() == FileStatus.ACTIVE
                )
                .verifyComplete();

        verify(minioS3Service, times(1)).putObjectToS3(anyString(), eq(multipartFile));
        verify(fileRepository, times(1)).save(fileEntity);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void updateFileStatus() {
        when(fileRepository.findById(anyLong())).thenReturn(Mono.just(fileEntity));

        Assertions.assertEquals(FileStatus.ACTIVE, fileEntity.getStatus());

        StepVerifier.create(fileService.updateFileStatus(123L, FileStatus.ARCHIVE))
                .verifyComplete();

        Assertions.assertEquals(FileStatus.ARCHIVE, fileEntity.getStatus());
    }

    @Test
    void deleteFile() {
        when(fileRepository.findById(anyLong())).thenReturn(Mono.just(fileEntity));
        when(minioS3Service.deleteObjectFromS3(fileEntity.getLocation())).thenReturn(Mono.just(mock(DeleteObjectResponse.class)));
        when(fileRepository.delete(fileEntity)).thenReturn(Mono.empty());
        when(eventRepository.save(any(EventEntity.class))).thenReturn(Mono.just(new EventEntity()));

        StepVerifier.create(fileService.deleteFile(123L))
                .verifyComplete();

        verify(minioS3Service, times(1)).deleteObjectFromS3(fileEntity.getLocation());
        verify(fileRepository, times(1)).delete(fileEntity);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }
}
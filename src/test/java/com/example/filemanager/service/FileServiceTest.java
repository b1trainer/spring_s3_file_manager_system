package com.example.filemanager.service;

import com.example.filemanager.config.S3Config;
import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.EventEntity;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.mapper.FileMapper;
import com.example.filemanager.repository.EventRepository;
import com.example.filemanager.repository.FileRepository;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    private static FileEntity fileEntity;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private MinioS3Service minioS3Service;

    @Mock
    private S3Config s3Config;

    @Mock
    private FileMapper fileMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeAll
    static void setUp() {
        fileEntity = new FileEntity();
        fileEntity.setId(1L);
        fileEntity.setName("test.txt");
        fileEntity.setStatus(FileDTO.FileStatus.ACTIVE);
        fileEntity.setLocation("bucket/test.txt");
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
    void loadFile() {
        String bucket = "test/location";

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test file content".getBytes()
        );

        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(1L);
        fileDTO.setName("test.txt");
        fileDTO.setStatus(FileDTO.FileStatus.ACTIVE);
        fileDTO.setLocation("bucket/test.txt");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("username");
        userEntity.setRole(UserDTO.UserRole.USER);
        userEntity.setStatus(UserDTO.UserStatus.ACTIVE);

        when(s3Config.getBucket()).thenReturn(bucket);
        when(userRepository.findById(anyLong())).thenReturn(Mono.just(userEntity));
        when(minioS3Service.putObjectToS3(anyString(), eq(multipartFile))).thenReturn(Mono.just(mock(PutObjectResponse.class)));
        when(fileMapper.map(any(FileDTO.class))).thenReturn(fileEntity);
        when(fileMapper.map(any(FileEntity.class))).thenReturn(fileDTO);
        when(fileRepository.save(any(FileEntity.class))).thenReturn(Mono.just(fileEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(Mono.just(mock(EventEntity.class)));

        StepVerifier.create(fileService.loadFile(multipartFile, 123L))
                .expectNextMatches(file -> file.getName().equals("test.txt")
                        && file.getStatus() == FileDTO.FileStatus.ACTIVE
                        && file.getLocation().equals("bucket/test.txt")
                )
                .verifyComplete();

        verify(minioS3Service, times(1)).putObjectToS3(anyString(), eq(multipartFile));
        verify(fileRepository, times(1)).save(fileEntity);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void updateFileStatus() {
        when(fileRepository.findById(anyLong())).thenReturn(Mono.just(fileEntity));

        Assertions.assertEquals(FileDTO.FileStatus.ACTIVE, fileEntity.getStatus());

        StepVerifier.create(fileService.updateFileStatus(123L, FileDTO.FileStatus.ARCHIVE))
                .verifyComplete();

        Assertions.assertEquals(FileDTO.FileStatus.ARCHIVE, fileEntity.getStatus());
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
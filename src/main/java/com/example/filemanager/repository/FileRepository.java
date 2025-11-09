package com.example.filemanager.repository;

import com.example.filemanager.entity.FileEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FileRepository extends R2dbcRepository<FileEntity, Long> {

    Flux<FileEntity> findAllByUserId(Long userId);
}

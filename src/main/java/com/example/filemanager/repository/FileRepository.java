package com.example.filemanager.repository;

import com.example.filemanager.entity.FileEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface FileRepository extends R2dbcRepository<FileEntity, Long> {

    Mono<List<FileEntity>> findAllByUserId(Long userId);
}

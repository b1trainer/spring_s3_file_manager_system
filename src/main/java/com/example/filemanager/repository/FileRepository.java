package com.example.filemanager.repository;

import com.example.filemanager.entity.FileEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends R2dbcRepository<FileEntity, Long> {
    FileEntity getFileEntityById(Long id);
}

package com.example.filemanager.repository;

import com.example.filemanager.dto.FileDTO;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FileRepository {
    String getFileLocation(String fileId);

    void saveFileInfo(FileDTO fileDTO);
}

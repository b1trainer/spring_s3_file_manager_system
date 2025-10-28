package com.example.filemanager.mapper;

import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.entity.FileEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDTO map(FileEntity fileEntity);

    @InheritInverseConfiguration
    FileEntity map(FileDTO fileDTO);
}

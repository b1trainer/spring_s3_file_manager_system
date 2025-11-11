package com.example.filemanager.mapper;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

public interface UserMapper {
    UserDTO map(UserEntity userEntity);

    UserEntity map(UserDTO userDTO);
}

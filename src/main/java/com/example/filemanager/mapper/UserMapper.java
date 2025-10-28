package com.example.filemanager.mapper;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO map(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity map(UserDTO userDTO);
}

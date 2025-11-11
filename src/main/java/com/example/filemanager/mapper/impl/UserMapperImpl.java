package com.example.filemanager.mapper.impl;

import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.EventEntity;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.mapper.EventMapper;
import com.example.filemanager.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    private final EventMapper eventMapper;

    public UserMapperImpl(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    @Override
    public UserDTO map(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setRole(userEntity.getRole());
        userDTO.setStatus(userEntity.getStatus());
        userDTO.setCreatedAt(userEntity.getCreatedAt().toString());

        List<EventDTO> events = userEntity.getEvents().stream().map(eventMapper::map).toList();
        userDTO.setEvents(events);

        return userDTO;
    }

    @Override
    public UserEntity map(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setRole(userDTO.getRole());
        userEntity.setStatus(userDTO.getStatus());

        if (userDTO.getEvents() != null) {
            Set<EventEntity> events = userDTO.getEvents().stream().map(eventMapper::map).collect(Collectors.toSet());
            userEntity.setEvents(events);
        }

        return userEntity;
    }
}

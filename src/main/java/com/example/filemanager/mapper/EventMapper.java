package com.example.filemanager.mapper;

import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.entity.EventEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDTO map(EventEntity eventEntity);

    @InheritInverseConfiguration
    EventEntity map(EventDTO eventDto);
}

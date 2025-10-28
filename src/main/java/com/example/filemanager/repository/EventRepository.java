package com.example.filemanager.repository;

import com.example.filemanager.entity.EventEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends R2dbcRepository<EventEntity, Long> {
}


package com.example.filemanager.repository;

import com.example.filemanager.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, Long> {

    @Query("SELECT * FROM users u LEFT JOIN events e ON u.id = e.user_id WHERE u.username = :username")
    Mono<UserEntity> findByUsername(@Param("username") String username);

    @Query("SELECT * FROM users u LEFT JOIN events e ON u.id = e.user_id WHERE u.id = :userId")
    Mono<UserEntity> findUserById(@Param("userId") Long userId);

}

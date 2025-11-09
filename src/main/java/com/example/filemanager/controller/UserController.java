package com.example.filemanager.controller;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.naming.ServiceUnavailableException;

@RestController
@RequestMapping("/rest/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured("ADMIN")
    @PostMapping
    public Mono<ResponseEntity<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO)
                .onErrorMap(RuntimeException.class, e -> new ServiceUnavailableException("Ошибка создания пользователя: " + e))
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserDTO>> getUser(@PathVariable Long userId) {
        return userService.getUser(userId)
                .onErrorMap(RuntimeException.class, e -> new ServiceUnavailableException("Ошибка получения пользователя: " + e))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{userId}")
    public Mono<ResponseEntity<Void>> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO)
                .onErrorMap(RuntimeException.class, e -> new ServiceUnavailableException("Ошибка обновления пользователя: " + e))
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @Secured("ADMIN")
    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId)
                .onErrorMap(RuntimeException.class, e -> new ServiceUnavailableException("Ошибка удаления пользователя: " + e))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}

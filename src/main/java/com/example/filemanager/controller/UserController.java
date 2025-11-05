package com.example.filemanager.controller;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserDTO>> getUser(@PathVariable Long userId) {
        return userService.getUser(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{userId}")
    public Mono<ResponseEntity<UserDTO>> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @Secured("ADMIN")
    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}

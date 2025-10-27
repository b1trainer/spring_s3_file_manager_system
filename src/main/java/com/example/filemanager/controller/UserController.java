package com.example.filemanager.controller;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Mono<ResponseEntity<UserDTO>> createUser() {}

    @GetMapping
    public Mono<ResponseEntity<UserDTO>> getUser() {}

    @PutMapping
    public Mono<ResponseEntity<UserDTO>> updateUser() {}

    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteUser() {}
}

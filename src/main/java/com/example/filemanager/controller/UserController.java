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
    public Mono<ResponseEntity<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserDTO>> getUser(@PathVariable String userId) {
        return userService.getUser(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public Mono<ResponseEntity<UserDTO>> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}

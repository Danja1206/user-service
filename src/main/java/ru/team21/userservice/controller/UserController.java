package ru.team21.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.team21.userservice.model.request.RegisterRequest;
import ru.team21.userservice.service.interfaces.UserService;

@RestController
@RequestMapping("/team21/api/v2/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService service;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @CrossOrigin("*")
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequest request) {
        logger.info("Register request: {}", request);
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @PostMapping("/getByJwt")
    public ResponseEntity<Object> getUserByJwt(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");

        if (!token.startsWith("Bearer ")) {
            return ResponseEntity.ok(service.getUserId(token));
        }

        return ResponseEntity.badRequest().build();
    }
}
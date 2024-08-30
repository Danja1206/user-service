package ru.team21.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.team21.userservice.model.request.AuthenticationRequest;
import ru.team21.userservice.model.response.AuthenticationResponse;
import ru.team21.userservice.service.interfaces.AuthenticationService;

@RestController
@RequestMapping("/team21/api/v2/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService service;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request) {
        logger.info("Authentication request: {}", request);
        return ResponseEntity.ok(service.authenticate(request));
    }
}

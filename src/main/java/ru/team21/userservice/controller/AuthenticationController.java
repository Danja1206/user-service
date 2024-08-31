package ru.team21.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/check-token")
    public ResponseEntity<Boolean> checkToken(@RequestHeader("Authorization") String token) {
        logger.info("Checking token: {}", token);

        if (!token.startsWith("Bearer ")) {
            logger.warn("Invalid token format: {}", token);
            return ResponseEntity.badRequest().build();
        }

        token = token.replace("Bearer ", "");
        logger.debug("Token after Bearer removal: {}", token);

        boolean isValid = service.verifyToken(token);
        logger.info("Token is valid: {}", isValid);

        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/check-username")
    public ResponseEntity<String> checkEmail(@RequestHeader("Authorization") String token) {
        System.out.println("Checking username: " + token);

        token = token.replace("Bearer ", "");

        System.out.println(token);

        if (!token.startsWith("Bearer ")) {
            return ResponseEntity.ok(service.verifyEmail(token));
        }

        return ResponseEntity.badRequest().build();
    }
}

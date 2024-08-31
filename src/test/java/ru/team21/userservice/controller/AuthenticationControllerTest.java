package ru.team21.userservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import ru.team21.userservice.model.request.AuthenticationRequest;
import ru.team21.userservice.model.response.AuthenticationResponse;
import ru.team21.userservice.service.interfaces.AuthenticationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    private AuthenticationService authenticationService;
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        authenticationService = Mockito.mock(AuthenticationService.class);
        authenticationController = new AuthenticationController(authenticationService);
    }

    @Test
    void authenticate_ShouldReturnAuthenticationResponse_WhenRequestIsValid() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("testUser");
        request.setPassword("testPassword");

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken("testToken");

        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(response);

        // Act
        ResponseEntity<AuthenticationResponse> result = authenticationController.authentication(request);

        // Assert
        assertEquals(ResponseEntity.ok(response), result);
        verify(authenticationService, times(1)).authenticate(any(AuthenticationRequest.class));
    }

    @Test
    void checkToken_ShouldReturnTrue_WhenTokenIsValid() {
        // Arrange
        String token = "validToken";
        when(authenticationService.verifyToken(anyString())).thenReturn(true);

        // Act
        ResponseEntity<Boolean> result = authenticationController.checkToken("Bearer " + token);

        // Assert
        assertEquals(ResponseEntity.ok(true), result);
        verify(authenticationService, times(1)).verifyToken(token);
    }

    @Test
    void checkToken_ShouldReturnBadRequest_WhenTokenIsInvalid() {
        // Act
        ResponseEntity<Boolean> result = authenticationController.checkToken("InvalidTokenFormat");

        // Assert
        assertEquals(ResponseEntity.badRequest().build(), result);
        verify(authenticationService, never()).verifyToken(anyString());
    }

    // More tests can be added here for other methods
}

package ru.team21.userservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import ru.team21.userservice.model.request.RegisterRequest;
import ru.team21.userservice.model.response.AuthenticationResponse;
import ru.team21.userservice.service.interfaces.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void register_ShouldReturnSuccess_WhenRequestIsValid() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setPassword("testPassword");
        request.setEmail("test@example.com");

        AuthenticationResponse expectedResponse = new AuthenticationResponse(); // Replace with your actual response type if needed

        when(userService.register(any(RegisterRequest.class))).thenReturn( expectedResponse);

        // Act
        ResponseEntity<Object> response = userController.register(request);

        // Assert
        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(userService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void getUserByJwt_ShouldReturnUserId_WhenTokenIsValid() {
        // Arrange
        String token = "validJwtToken";
        Long expectedUserId = 123L; // Replace with actual return type

        when(userService.getUserId(anyString())).thenReturn(expectedUserId);

        // Act
        ResponseEntity<Object> response = userController.getUserByJwt("Bearer " + token);

        // Assert
        assertEquals(ResponseEntity.ok(expectedUserId), response);
        verify(userService, times(1)).getUserId(token);
    }

    @Test
    void getUserByJwt_ShouldReturnBadRequest_WhenTokenIsInvalid() {
        // Act
        ResponseEntity<Object> response = userController.getUserByJwt("InvalidToken");

        // Assert
        assertEquals(ResponseEntity.badRequest().build(), response);
        verify(userService, never()).getUserId(anyString());
    }
}

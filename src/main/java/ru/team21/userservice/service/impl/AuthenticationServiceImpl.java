package ru.team21.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.team21.userservice.model.MyUser;
import ru.team21.userservice.model.exception.AuthenticationException;
import ru.team21.userservice.model.request.AuthenticationRequest;
import ru.team21.userservice.model.response.AuthenticationResponse;
import ru.team21.userservice.service.BaseService;
import ru.team21.userservice.service.JwtService;
import ru.team21.userservice.service.interfaces.AuthenticationService;
import ru.team21.userservice.service.interfaces.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl extends BaseService implements AuthenticationService {


    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            logger.info("Authenticating user {}", authenticationRequest.getEmail());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword())
            );

            var user = userService.getByEmail(authenticationRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            logger.info("User authenticated: {}", user.getEmail());
            return generateAuthenticationResponse(user);
        } catch (BadCredentialsException e) {
            logger.info("Authentication failed: User not found for email {}", authenticationRequest.getEmail());
            throw new AuthenticationException("Bad credentials", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Authenticate failed {}", e.getMessage());
            throw new RuntimeException("Authenticate failed ", e);
        }
    }

    private AuthenticationResponse generateAuthenticationResponse(MyUser user) {
        var jwtToken = jwtService.generateToken(user);
        logger.info("Generated JWT token for user: {}", user.getUsername());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public boolean verifyToken(String token) {
        logger.info("Verifying token {}", token);
        return jwtService.validateToken(token);
    }

    @Override
    public String verifyEmail(String token) {
        logger.info("Verifying email {}", token);
        return jwtService.extractUsername(token);
    }


}

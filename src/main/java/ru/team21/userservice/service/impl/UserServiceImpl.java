package ru.team21.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.team21.userservice.model.MyUser;
import ru.team21.userservice.model.Role;
import ru.team21.userservice.model.exception.UserAlreadyExistsException;
import ru.team21.userservice.model.request.RegisterRequest;
import ru.team21.userservice.model.response.AuthenticationResponse;
import ru.team21.userservice.repository.UserRepository;
import ru.team21.userservice.service.BaseService;
import ru.team21.userservice.service.JwtService;
import ru.team21.userservice.service.interfaces.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService implements UserService {


    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public MyUser getUserById(Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.getCredentials() instanceof String) {
//            String token = (String) authentication.getCredentials();
//
//            Long id = jwtService.extractUserId(token);
//
//            return repository.findById(id).orElse(null);
//        } else {
//            logger.error("User id not found");
//        }

        return repository.findById(id).orElse(null);

    }

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        logger.info("Registering new user with: {}", registerRequest.getEmail());

        Optional<MyUser> checkUser = repository.getByEmail(registerRequest.getEmail());

        if (checkUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        var user = MyUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);

        logger.info("User {} registered", user.getEmail());
        return generateAuthenticationResponse(user);
    }

    @Override
    public Optional<MyUser> getByEmail(String email) {
        return repository.getByEmail(email);
    }

    private AuthenticationResponse generateAuthenticationResponse(MyUser user) {
        var jwtToken = jwtService.generateToken(user);
        logger.info("Generated JWT token for user: {}", user.getUsername());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public Long getUserId(String token) {
        logger.info("Retrieving user id from token: {}", token);
        return jwtService.extractUserId(token);
    }
}

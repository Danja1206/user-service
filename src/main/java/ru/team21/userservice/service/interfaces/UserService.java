package ru.team21.userservice.service.interfaces;

import ru.team21.userservice.model.MyUser;
import ru.team21.userservice.model.request.RegisterRequest;
import ru.team21.userservice.model.response.AuthenticationResponse;

import java.util.Optional;

public interface UserService {

    MyUser getUserById(Long id);

    Optional<MyUser> getByEmail(String email);

    AuthenticationResponse register(RegisterRequest registerRequest);

}

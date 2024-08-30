package ru.team21.userservice.service.interfaces;

import ru.team21.userservice.model.request.AuthenticationRequest;
import ru.team21.userservice.model.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}

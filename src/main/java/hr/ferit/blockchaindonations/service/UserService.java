package hr.ferit.blockchaindonations.service;

import hr.ferit.blockchaindonations.dto.AuthenticationResponse;
import hr.ferit.blockchaindonations.model.User;
import hr.ferit.blockchaindonations.request.AuthenticationRequest;
import hr.ferit.blockchaindonations.request.RegistrationRequest;

public interface UserService {
    AuthenticationResponse register(RegistrationRequest registrationRequest);
    AuthenticationResponse authenticate(AuthenticationRequest request);

    User getUser(Long id);
}

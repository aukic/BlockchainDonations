package hr.ferit.blockchaindonations.service;

import hr.ferit.blockchaindonations.configuration.JwtService;
import hr.ferit.blockchaindonations.dto.AuthenticationResponse;
import hr.ferit.blockchaindonations.enums.AppUserRole;
import hr.ferit.blockchaindonations.model.User;
import hr.ferit.blockchaindonations.model.Wallet;
import hr.ferit.blockchaindonations.repository.UserRepository;
import hr.ferit.blockchaindonations.request.AuthenticationRequest;
import hr.ferit.blockchaindonations.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public AuthenticationResponse register(RegistrationRequest registrationRequest) {

        User existingUser = userRepository.findByEmail(registrationRequest.getEmail()).orElse(null);
        if(!Objects.isNull(existingUser)){
            throw new IllegalStateException("Email is taken!");
        }
        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        Wallet wallet = new Wallet();
        User user = new User(
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                encodedPassword,
                AppUserRole.USER,
                wallet
        );
        wallet.setUser(user);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}

package com.registration.service;

import com.registration.dto.LoginRequest;
import com.registration.entity.JwtToken;
import com.registration.entity.User;
import com.registration.exception.InvalidCredentialsException;
import com.registration.repository.JwtTokenRepository;
import com.registration.repository.UserRepository;
import com.registration.security.JwtUtil;
import com.registration.security.LoginResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       JwtTokenRepository jwtTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResult login(LoginRequest request) {
        User user = userRepository.findByName(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getName());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plus(jwtUtil.getExpirationMs(), ChronoUnit.MILLIS);
        JwtToken jwtToken = new JwtToken(user.getId(), token, now, expiresAt);
        jwtTokenRepository.save(jwtToken);

        return new LoginResult(user.getName(), token);
    }
}
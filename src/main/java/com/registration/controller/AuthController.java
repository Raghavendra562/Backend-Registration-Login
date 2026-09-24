package com.registration.controller;

import com.registration.dto.ApiResponse;
import com.registration.dto.LoginRequest;
import com.registration.security.JwtUtil;
import com.registration.security.LoginResult;
import com.registration.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final boolean cookieSecure;
    private final String cookieSameSite;

    public AuthController(AuthService authService,
                          JwtUtil jwtUtil,
                          @Value("${app.cookie.secure:false}") boolean cookieSecure,
                          @Value("${app.cookie.same-site:Lax}") String cookieSameSite) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.cookieSecure = cookieSecure;
        this.cookieSameSite = cookieSameSite;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = authService.login(request);

        ResponseCookie jwtCookie = ResponseCookie.from("JWT", result.token())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(jwtUtil.getExpirationMs() / 1000)
                .sameSite(cookieSameSite)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(Map.of(
                        "message", "Login successful",
                        "username", result.username()
                ));
    }
}
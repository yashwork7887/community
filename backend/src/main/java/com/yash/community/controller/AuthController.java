package com.yash.community.controller;

import com.yash.community.model.Models.User;
import com.yash.community.repository.UserRepository;
import com.yash.community.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class AuthController {

    public record SignUpRequest(String username, String password, String email) {}
    public record LoginRequest(String username, String password) {}

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignUpRequest req) {
        try {
            users.create(req.username(), encoder.encode(req.password()), req.email());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/log-in")
    public Map<String, String> logIn(@RequestBody LoginRequest req) {
        User user = users.findByUsername(req.username()).orElse(null);
        if (user == null || req.password() == null || !encoder.matches(req.password(), user.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return Map.of("token", jwt.generate(user.id(), user.username()));
    }
}

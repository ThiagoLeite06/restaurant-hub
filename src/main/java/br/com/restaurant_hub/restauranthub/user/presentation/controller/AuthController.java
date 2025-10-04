package br.com.restaurant_hub.restauranthub.user.presentation.controller;

import br.com.restaurant_hub.restauranthub.security.JwtUtils;
import br.com.restaurant_hub.restauranthub.user.application.dto.CreateUserRequest;
import br.com.restaurant_hub.restauranthub.user.application.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.user.application.usecase.CreateUserUseCase;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CreateUserUseCase createUserUseCase;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(CreateUserUseCase createUserUseCase,
                         AuthenticationManager authenticationManager, 
                         JwtUtils jwtUtils) {
        this.createUserUseCase = createUserUseCase;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = createUserUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", loginRequest.login()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new JwtResponse("", "", "Invalid credentials"));
        }
    }

    // DTOs for API
    public record LoginRequest(String login, String password) {}

    public record JwtResponse(String token, String type, String username) {}
}
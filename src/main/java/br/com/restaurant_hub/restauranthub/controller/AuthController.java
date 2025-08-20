package br.com.restaurant_hub.restauranthub.controller;

import br.com.restaurant_hub.restauranthub.mapper.UserMapper;
import br.com.restaurant_hub.restauranthub.entity.UserEntity;
import br.com.restaurant_hub.restauranthub.controller.dto.CreateUserRequest;
import br.com.restaurant_hub.restauranthub.controller.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.security.JwtUtils;
import br.com.restaurant_hub.restauranthub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserMapper userMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest userRequest) {
        UserEntity savedUser = userService.createUser(userRequest);
        UserResponse response = userMapper.toResponse(savedUser);

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

    public record LoginRequest(String login, String password) {
    }

    public record JwtResponse(String token, String type, String username) {
    }

}

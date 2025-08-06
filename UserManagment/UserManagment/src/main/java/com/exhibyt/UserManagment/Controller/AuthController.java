package com.exhibyt.UserManagment.Controller;

import com.exhibyt.UserManagment.Comman.ApiResponse;
import com.exhibyt.UserManagment.Dto.LoginRequest;
import com.exhibyt.UserManagment.Dto.LoginResponse;
import com.exhibyt.UserManagment.Dto.RegisterRequest;
import com.exhibyt.UserManagment.Services.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.debug(request.toString());
        log.info("Login request received for username: {}", request.getUsername());
        LoginResponse response = authService.login(request);
        log.info("Login successful for username: {}", request.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request received for username: {}", request.getUsername());
        authService.register(request);
        log.info("User registered successfully: {}", request.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "User registered successfully", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@RequestParam String refreshToken) {
        log.info("Refresh token request received");
        LoginResponse response = authService.refreshToken(refreshToken);
        log.info("Access token refreshed successfully");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Access token refreshed successfully", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestParam String refreshToken) {
        log.info("Logout request received");
        authService.logout(refreshToken);
        log.info("Logout successful");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Logout successful", null));
    }
}

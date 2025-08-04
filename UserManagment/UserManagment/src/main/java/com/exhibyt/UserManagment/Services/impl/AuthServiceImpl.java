package com.exhibyt.UserManagment.Services.impl;

import com.exhibyt.UserManagment.Constant.RoleName;
import com.exhibyt.UserManagment.Dto.LoginRequest;
import com.exhibyt.UserManagment.Dto.LoginResponse;
import com.exhibyt.UserManagment.Dto.RegisterRequest;
import com.exhibyt.UserManagment.Entity.RefreshToken;
import com.exhibyt.UserManagment.Entity.Role;
import com.exhibyt.UserManagment.Entity.User;
import com.exhibyt.UserManagment.Exception.TokenExpiredException;
import com.exhibyt.UserManagment.Exception.UserAlreadyExistsException;
import com.exhibyt.UserManagment.Repository.RefreshTokenRepository;
import com.exhibyt.UserManagment.Repository.RoleRepository;
import com.exhibyt.UserManagment.Repository.UserRepository;
import com.exhibyt.UserManagment.Services.AuthService;
import com.exhibyt.UserManagment.Services.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Attempting login for user: {}", request.getEmail());

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found with email {}", request.getEmail());
                    return new UsernameNotFoundException("User not found with email: " + request.getEmail());
                });

        log.info("Login successful for user: {}", request.getEmail());
        return generateTokensForUser(user);
    }

    @Override
    public void register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepo.existsByUsername(request.getUsername())) {
            log.warn("Registration failed: Username '{}' already exists", request.getUsername());
            throw new UserAlreadyExistsException("Username '" + request.getUsername() + "' already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepo.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> {
                    log.error("Default role '{}' not found during registration", RoleName.ROLE_USER);
                    return new RuntimeException("Default role not found");
                });

        user.setRoles(Set.of(userRole));
        userRepo.save(user);

        log.info("User '{}' registered successfully", request.getUsername());
    }

    @Override
    public LoginResponse refreshToken(String refreshTokenStr) {
        log.info("Attempting to refresh access token using refresh token");

        RefreshToken token = refreshTokenRepo.findByToken(refreshTokenStr)
                .orElseThrow(() -> {
                    log.warn("Invalid refresh token used: {}", refreshTokenStr);
                    return new IllegalArgumentException("Invalid refresh token");
                });

        if (jwtService.isRefreshTokenExpired(token)) {
            log.warn("Refresh token expired for user: {}", token.getUser().getUsername());
            throw new TokenExpiredException("Refresh token expired");
        }

        String newAccessToken = jwtService.generateToken(token.getUser());
        log.info("Access token refreshed for user: {}", token.getUser().getUsername());

        return new LoginResponse(newAccessToken, refreshTokenStr);
    }

    @Transactional
    @Override
    public void logout(String refreshTokenStr) {
        log.info("Logging out user with refresh token");
        refreshTokenRepo.deleteByToken(refreshTokenStr);
        log.info("User successfully logged out");
    }

    private LoginResponse generateTokensForUser(User user) {
        log.debug("Generating JWT tokens for user: {}", user.getUsername());
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        log.debug("Tokens generated for user: {}", user.getUsername());
        return new LoginResponse(accessToken, refreshToken);
    }
}

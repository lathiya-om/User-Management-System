package com.exhibyt.UserManagment.Services;

import com.exhibyt.UserManagment.Dto.LoginRequest;
import com.exhibyt.UserManagment.Dto.LoginResponse;
import com.exhibyt.UserManagment.Dto.RegisterRequest;

public interface AuthService {

    /**
     * Authenticates a user and returns JWT access and refresh tokens.
     * @param request The login credentials.
     * @return The access and refresh tokens.
     */
    LoginResponse login(LoginRequest request);

    /**
     * Registers a new user with default role(s).
     * @param request The registration details.
     */
    void register(RegisterRequest request);

    /**
     * Generates a new access token using a valid refresh token.
     * @param refreshToken The refresh token string.
     * @return A new access token with the same refresh token.
     */
    LoginResponse refreshToken(String refreshToken);

    /**
     * Invalidates the refresh token (logout operation).
     * @param refreshToken The token to invalidate.
     */
    void logout(String refreshToken);
}

package com.exhibyt.UserManagment.Controller;

import com.exhibyt.UserManagment.Comman.ApiResponse;
import com.exhibyt.UserManagment.Dto.UpdateUserRequest;
import com.exhibyt.UserManagment.Dto.UserProfileDto;
import com.exhibyt.UserManagment.Services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')") // ✅ All endpoints require at least ROLE_USER
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get the current user's profile.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getProfile() {
        log.info("Fetching current user profile");
        UserProfileDto profile = userService.getCurrentUserProfile();
        log.info("User profile fetched successfully for userId={}", profile.getEmail());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "User profile fetched", profile));
    }

    /**
     * Update the current user's profile.
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@Valid @RequestBody UpdateUserRequest request) {
        log.info("Update request received for current user: {}", request);
        userService.updateCurrentUser(request);
        log.info("User profile updated successfully");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "User profile updated", null));
    }

    /**
     * Soft delete the current user's account.
     */
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> softDelete() {
        log.info("Soft delete request received for current user");
        userService.softDeleteCurrentUser();
        log.info("User account soft-deleted successfully");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "User account deleted (soft)", null));
    }

    /**
     * Disable the current user's account.
     */
    @PutMapping("/me/disable")
    public ResponseEntity<ApiResponse<Void>> disableAccount() {
        log.info("Disable account request received for current user");
        userService.disableCurrentUser();
        log.info("User account disabled successfully");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "User account disabled", null));
    }

    /**
     * Enable the current user's account (if previously disabled).
     */
    @PutMapping("/me/enable")
    public ResponseEntity<ApiResponse<Void>> enableAccount() {
        log.info("Enable account request received for current user");
        userService.enableCurrentUser();
        log.info("User account enabled successfully");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "User account enabled", null));
    }
}

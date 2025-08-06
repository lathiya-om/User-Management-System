package com.exhibyt.UserManagment.Controller;

import com.exhibyt.UserManagment.Comman.ApiResponse;
import com.exhibyt.UserManagment.Constant.RoleName;
import com.exhibyt.UserManagment.Dto.RegisterRequest;
import com.exhibyt.UserManagment.Services.AdminService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> createAdmin(@Valid @RequestBody RegisterRequest request) {
        log.info("Admin creation request received for username: {}", request.getUsername());
        adminService.createAdmin(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Admin user created", null));
    }

    @PostMapping("/promote/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> promoteToAdmin(@PathVariable String username) {
        log.info("Promote to ADMIN request received for user: {}", username);
        adminService.promoteToAdmin(username);
        return ResponseEntity
                .ok(new ApiResponse<>(HttpStatus.OK.value(), "User promoted to ADMIN", null));
    }

    @PatchMapping("/soft-delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> softDeleteUser(@PathVariable Long userId) {
        log.info("Soft delete request for userId: {}", userId);
        adminService.softDeleteUser(userId);
        return ResponseEntity
                .ok(new ApiResponse<>(HttpStatus.OK.value(), "User soft deleted successfully", null));
    }

    @PatchMapping("/enable-disable/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> enableOrDisableUser(
            @PathVariable Long userId,
            @RequestParam boolean enable
    ) {
        log.info("Enable/Disable user request: userId={}, enable={}", userId, enable);
        adminService.enableOrDisableUser(userId, enable);
        return ResponseEntity
                .ok(new ApiResponse<>(HttpStatus.OK.value(),
                        enable ? "User enabled" : "User disabled", null));
    }

    @PostMapping("/assign-role/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignRole(
            @PathVariable Long userId,
            @RequestParam RoleName roleName
    ) {
        log.info("Assign role request: userId={}, role={}", userId, roleName);
        adminService.assignRole(userId, roleName);
        return ResponseEntity
                .ok(new ApiResponse<>(HttpStatus.OK.value(), "Role assigned successfully", null));
    }

    @DeleteMapping("/revoke-role/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> revokeRole(
            @PathVariable Long userId,
            @RequestParam RoleName roleName
    ) {
        log.info("Revoke role request: userId={}, role={}", userId, roleName);
        adminService.revokeRole(userId, roleName);
        return ResponseEntity
                .ok(new ApiResponse<>(HttpStatus.OK.value(), "Role revoked successfully", null));
    }
}

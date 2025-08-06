package com.exhibyt.UserManagment.Services.impl;

import com.exhibyt.UserManagment.Constant.RoleName;
import com.exhibyt.UserManagment.Dto.RegisterRequest;
import com.exhibyt.UserManagment.Entity.Role;
import com.exhibyt.UserManagment.Entity.User;
import com.exhibyt.UserManagment.Repository.RoleRepository;
import com.exhibyt.UserManagment.Repository.UserRepository;
import com.exhibyt.UserManagment.Services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createAdmin(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User already exists with username: " + request.getUsername());
        }

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setDeleted(false);
        user.getRoles().add(adminRole);

        userRepository.save(user);
        log.info("✅ ADMIN user created: {}", request.getUsername());
    }

    @Override
    public void promoteToAdmin(String username) {
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        if (user.getRoles().contains(adminRole)) {
            log.warn("User '{}' is already an ADMIN", username);
            return;
        }

        user.getRoles().add(adminRole);
        userRepository.save(user);
        log.info("✅ User '{}' promoted to ADMIN", username);
    }

    @Override
    public void softDeleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setDeleted(true);
        userRepository.save(user);
        log.info("🗑️ User soft-deleted: {}", user.getUsername());
    }

    @Override
    public void enableOrDisableUser(Long userId, boolean enable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setEnabled(enable);
        userRepository.save(user);
        log.info("🔄 User '{}' {}", user.getUsername(), enable ? "enabled" : "disabled");
    }

    @Override
    public void assignRole(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        if (user.getRoles().contains(role)) {
            log.warn("User '{}' already has role {}", user.getUsername(), roleName);
            return;
        }

        user.getRoles().add(role);
        userRepository.save(user);
        log.info("✅ Role '{}' assigned to user '{}'", roleName, user.getUsername());
    }

    @Override
    public void revokeRole(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        if (!user.getRoles().contains(role)) {
            log.warn("User '{}' does not have role {}", user.getUsername(), roleName);
            return;
        }

        user.getRoles().remove(role);
        userRepository.save(user);
        log.info("❌ Role '{}' revoked from user '{}'", roleName, user.getUsername());
    }
}

package com.exhibyt.UserManagment.Services.impl;

import com.exhibyt.UserManagment.Configration.UserMapper;
import com.exhibyt.UserManagment.Dto.UpdateProfileRequest;
import com.exhibyt.UserManagment.Dto.UpdateUserRequest;
import com.exhibyt.UserManagment.Dto.UserProfileDto;
import com.exhibyt.UserManagment.Entity.User;
import com.exhibyt.UserManagment.Exception.ResourceNotFoundException;
import com.exhibyt.UserManagment.Exception.UsernameNotFoundException;
import com.exhibyt.UserManagment.Repository.UserRepository;
import com.exhibyt.UserManagment.Services.UserService;
import com.exhibyt.UserManagment.Utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile() {
        String currentUsername = SecurityUtils.getCurrentUsername();

        log.info("Fetching profile for current user: {}", currentUsername);

        User user = userRepository.findByUsernameAndDeletedFalse(currentUsername)
                .orElseThrow(() -> {
                    log.warn("Profile fetch failed: User not found with username {}", currentUsername);
                    return new UsernameNotFoundException("User not found with username: " + currentUsername);
                });

        log.info("Profile fetched successfully for user: {}", currentUsername);

        return userMapper.toUserProfileDto(user);
    }


    @Override
    @Transactional
    public void updateCurrentUser(UpdateUserRequest request) {
        User user = SecurityUtils.getCurrentUser(userRepository);

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        log.info("Updated current user profile: {}", user.getId());
    }

    @Override
    @Transactional
    public void softDeleteCurrentUser() {
        User user = SecurityUtils.getCurrentUser(userRepository);
        user.setDeleted(true);
        userRepository.save(user);
        log.info("Soft deleted user: {}", user.getId());
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        log.info("Admin updated user with id: {}", userId);
    }

    @Override
    @Transactional
    public void disableCurrentUser() {
        User user = SecurityUtils.getCurrentUser(userRepository);
        user.setEnabled(false);
        userRepository.save(user);
        log.info("Disabled user: {}", user.getId());
    }

    @Override
    @Transactional
    public void enableCurrentUser() {
        User user = SecurityUtils.getCurrentUser(userRepository);
        user.setEnabled(true);
        userRepository.save(user);
        log.info("Enabled user: {}", user.getId());
    }
}

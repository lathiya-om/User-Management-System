package com.exhibyt.UserManagment.Services;

import com.exhibyt.UserManagment.Entity.User;
import com.exhibyt.UserManagment.Exception.CustomUsernameNotFoundException;
import com.exhibyt.UserManagment.Exception.ResourceNotFoundException;
import com.exhibyt.UserManagment.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> {
                    log.warn("User not found or is soft-deleted: {}", username);
                    return new CustomUsernameNotFoundException("User not found with username: " + username);
                });

        log.info("User '{}' found. Preparing UserDetails object.", username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .toArray(String[]::new))
                .accountLocked(!user.isEnabled())
                .disabled(!user.isEnabled())
                .build();
    }

}

package com.exhibyt.UserManagment;

import com.exhibyt.UserManagment.Constant.RoleName;
import com.exhibyt.UserManagment.Entity.Role;
import com.exhibyt.UserManagment.Entity.User;
import com.exhibyt.UserManagment.Repository.RoleRepository;
import com.exhibyt.UserManagment.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@SpringBootApplication
public class UserManagmentApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger log = LoggerFactory.getLogger(UserManagmentApplication.class);

	private static final String DEFAULT_SUPER_ADMIN_USERNAME = "superadmin";
	private static final String DEFAULT_SUPER_ADMIN_EMAIL = "superadmin@example.com";
	private static final String DEFAULT_SUPER_ADMIN_PASSWORD = "superadmin123"; // 🔐 Change after first login!

	public static void main(String[] args) {
		SpringApplication.run(UserManagmentApplication.class, args);
	}

	@Override
	public void run(String... args) {
		initializeRoles();
		initializeSuperAdmin();
	}

	private void initializeRoles() {
		log.info("Initializing default roles...");
		for (RoleName roleName : RoleName.values()) {
			if (!roleRepository.existsByName(roleName)) {
				Role role = new Role();
				role.setName(roleName);
				roleRepository.save(role);
				log.info("Created role: {}", roleName);
			} else {
				log.debug("Role already exists: {}", roleName);
			}
		}
		log.info("✅ Default roles initialized successfully.");
	}

	private void initializeSuperAdmin() {
		if (userRepository.existsByUsername(DEFAULT_SUPER_ADMIN_USERNAME)) {
			log.info("SUPER_ADMIN user already exists: {}", DEFAULT_SUPER_ADMIN_USERNAME);
			return;
		}

		Role superAdminRole = roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN)
				.orElseThrow(() -> new RuntimeException("ROLE_SUPER_ADMIN not found"));

		User superAdmin = new User();
		superAdmin.setUsername(DEFAULT_SUPER_ADMIN_USERNAME);
		superAdmin.setEmail(DEFAULT_SUPER_ADMIN_EMAIL);
		superAdmin.setPassword(passwordEncoder.encode(DEFAULT_SUPER_ADMIN_PASSWORD));
		superAdmin.setRoles(Collections.singleton(superAdminRole));
		superAdmin.setEnabled(true);
		superAdmin.setDeleted(false);

		userRepository.save(superAdmin);

		log.info("✅ Default SUPER_ADMIN created:");
		log.info("   - Username: {}", DEFAULT_SUPER_ADMIN_USERNAME);
		log.info("   - Email: {}", DEFAULT_SUPER_ADMIN_EMAIL);
		log.info("   - Password: {}", DEFAULT_SUPER_ADMIN_PASSWORD);
	}
}

package com.exhibyt.UserManagment;

import com.exhibyt.UserManagment.Constant.RoleName;
import com.exhibyt.UserManagment.Entity.Role;
import com.exhibyt.UserManagment.Repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class UserManagmentApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(UserManagmentApplication.class, args);
	}

	@Override
	public void run(String... args) {
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
}

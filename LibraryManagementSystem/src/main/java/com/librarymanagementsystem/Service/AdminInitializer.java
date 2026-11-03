package com.librarymanagementsystem.Service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.librarymanagementsystem.Entity.Categories;
import com.librarymanagementsystem.Entity.Roles;
import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Repository.CategoriesRepository;
import com.librarymanagementsystem.Repository.RolesRepository;
import com.librarymanagementsystem.Repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

	private final UsersRepository userRepository;
	private final RolesRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final CategoriesRepository categoriesRepository;
	@Value("${app.bootstrap-admin.email:}")
	private String bootstrapAdminEmail;
	@Value("${app.bootstrap-admin.password:}")
	private String bootstrapAdminPassword;
	private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

	@Override
	public void run(String... args) {
		logger.info("Checking if ADMIN role is present or not");
		Roles adminRole = roleRepository.findById(1).orElseGet(() -> {
			Roles role = new Roles();
			role.setStatus(true);
			role.setRole("ADMIN");
			return roleRepository.save(role);
		});
		logger.info("Checking if LIBRARIAN role is present or not");
		Roles librarianRole = roleRepository.findById(2).orElseGet(() -> {
			Roles role = new Roles();
			role.setRole("LIBRARIAN");
			role.setStatus(true);
			return roleRepository.save(role);
		});
		if (bootstrapAdminEmail.isBlank() != bootstrapAdminPassword.isBlank()) {
			throw new IllegalStateException("Set both BOOTSTRAP_ADMIN_EMAIL and BOOTSTRAP_ADMIN_PASSWORD, or leave both empty");
		}
		if (!bootstrapAdminEmail.isBlank() && !userRepository.existsById(bootstrapAdminEmail)) {
			Users admin = new Users();	
			admin.setEmail(bootstrapAdminEmail);
			admin.setPassword(passwordEncoder.encode(bootstrapAdminPassword));
			admin.setVerified(true);
			admin.setStatus(true);
			admin.setRoles(Set.of(adminRole));
			userRepository.save(admin);
			logger.info("Bootstrap administrator created for {}", bootstrapAdminEmail);
		}
		createCategoryIfNotExists("ACTION");
		createCategoryIfNotExists("DRAMA");
		createCategoryIfNotExists("THRILLER");
	}

	private void createCategoryIfNotExists(String name) {
		Optional<Categories> categoryy = categoriesRepository.findByCategoryName(name);
		if (categoryy.isEmpty()) {
			Categories category = new Categories();
			category.setStatus(true);
			category.setCategoryName(name);
			categoriesRepository.save(category);
			logger.info("Category created: {}", name);
		}
	}

}

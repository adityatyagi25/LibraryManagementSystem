package com.librarymanagementsystem.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.DTO.UsersDTO;
import com.librarymanagementsystem.Entity.Roles;
import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Repository.RolesRepository;
import com.librarymanagementsystem.Repository.UsersRepository;
import com.librarymanagementsystem.Repository.VerificationTokenRepo;

import jakarta.transaction.Transactional;

@Service
public class UsersService {
	@Autowired
	private VerificationTokenService verificationTokenService;
	@Autowired
	private RolesRepository rolesRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private VerificationTokenRepo tokenRepository;

    @Transactional
	public ResponseEntity<String> addUser(UsersDTO userDTO) {

		Optional<Users> existingUserOptional = usersRepository.findById(userDTO.getEmail());

		if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
			return new ResponseEntity<>("Password can't be null or empty", HttpStatus.OK);
		}

		if (userDTO.getEmail().length() >= 100 || userDTO.getPassword().length() >= 100) {
			return new ResponseEntity<>("Please enter characters less than 100", HttpStatus.OK);
		}

		String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";
		if (!userDTO.getEmail().matches(regex)) {
			return new ResponseEntity<>("Please check the format of your mail id !!", HttpStatus.OK);
		}

		// Build roles
		Set<Roles> rolesSet = new HashSet<>();
		for (String roleName : userDTO.getRoles()) {
			Optional<Roles> roleEntity = rolesRepository.findByRole(roleName);

			if (roleEntity.isPresent()) {
				rolesSet.add(roleEntity.get());
			} else {
				return new ResponseEntity<>("Invalid role: " + roleName + ". Allowed roles are ADMIN and LIBRARIAN",
						HttpStatus.OK);
			}
		}

		if (existingUserOptional.isPresent()) {
			Users existingUser = existingUserOptional.get();

			// User already active
			if (existingUser.isStatus()) {
				return new ResponseEntity<>("User Already Exists", HttpStatus.OK);
			}

			// User exists but inactive → reactivate
			existingUser.setStatus(true);
			existingUser.setVerified(false);
			existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
			existingUser.getRoles().clear();
			existingUser.getRoles().addAll(rolesSet);
			usersRepository.save(existingUser);

			String token = verificationTokenService.generateVerificationToken(existingUser);
			verificationTokenService.sendVerificationEmail(existingUser.getEmail(), token);

			return new ResponseEntity<>("User reactivated successfully", HttpStatus.OK);
		}

		// New user
		Users user = new Users();
		user.setEmail(userDTO.getEmail());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setStatus(true);
		user.setVerified(false);
		user.setRoles(rolesSet);

		usersRepository.save(user);

		String token = verificationTokenService.generateVerificationToken(user);
		verificationTokenService.sendVerificationEmail(user.getEmail(), token);

		return new ResponseEntity<>("User Added Successfully", HttpStatus.OK);
	}

    @Transactional
	public ResponseEntity<String> deleteUser(String email) {

		Optional<Users> userOptional = usersRepository.findById(email);

		if (userOptional.isEmpty()) {
			return new ResponseEntity<>("User not found", HttpStatus.OK);
		}

		Users user = userOptional.get();

		if (!user.isStatus()) {
			return new ResponseEntity<>("User already deleted", HttpStatus.OK);
		}

		user.setStatus(false);
		user.setVerified(false);
		tokenRepository.deleteByUser_Email(email);

		usersRepository.save(user);

		return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
	}

}

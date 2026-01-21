package com.librarymanagementsystem.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.LoginDTO;
import com.librarymanagementsystem.Repository.UsersRepository;
import com.librarymanagementsystem.Security.JwtUtil;
import com.librarymanagementsystem.Security.UserPrinciple;

@RestController
public class AuthController {

	private final AuthenticationManager authManager;
	private final JwtUtil jwtUtil;
	@Autowired
	private UsersRepository usersRepository;

	public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO login) {
		String email = login.getEmail();
		String password = login.getPassword();
		if (usersRepository.findById(email).isEmpty()) {
			return new ResponseEntity<>("User not Found with id " + email, HttpStatus.OK);
		}
		if (usersRepository.findById(email).get().isVerified() == false) {
			return new ResponseEntity<>("User not Verified", HttpStatus.OK);
		}

		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, password));

			UserPrinciple user = (UserPrinciple) authentication.getPrincipal();
			String token = jwtUtil.generateToken(user);

			// Successful response
			return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Login successful", "token", token));

		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>("Incorrect Password", HttpStatus.OK);

		} catch (Exception ex) {
			// ⚠️ Unexpected error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Something went wrong", "details", ex.getMessage()));
		}
	}
}

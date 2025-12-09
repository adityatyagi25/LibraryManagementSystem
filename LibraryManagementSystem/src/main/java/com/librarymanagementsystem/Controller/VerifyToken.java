package com.librarymanagementsystem.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Entity.VerificationToken;
import com.librarymanagementsystem.Repository.UsersRepository;
import com.librarymanagementsystem.Repository.VerificationTokenRepo;

@RestController
public class VerifyToken {
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private VerificationTokenRepo tokenRepository;
	@GetMapping("/verify")
	public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {

	    VerificationToken verificationToken = tokenRepository.findByToken(token);

	    if (verificationToken == null) {
	        return ResponseEntity.badRequest().body("Invalid token");
	    }

	    if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
	        return ResponseEntity.badRequest().body("Token expired");
	    }

	    Users user = verificationToken.getUser();
	    user.setVerified(true);
	    usersRepository.save(user);

	    return ResponseEntity.ok("Email verified successfully!");
	}

}

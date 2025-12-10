package com.librarymanagementsystem.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.Entity.Status;
import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Entity.VerificationToken;
import com.librarymanagementsystem.Repository.VerificationTokenRepo;
@Service
public class VerificationTokenService {
	@Autowired
	private VerificationTokenRepo tokenRepository;
	public String generateVerificationToken(Users user) {
	    String token = UUID.randomUUID().toString();

	    VerificationToken verificationToken = new VerificationToken();
	    verificationToken.setToken(token);
	    verificationToken.setUser(user);
	    verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
	    verificationToken.setStatus(Status.PENDING);
	    tokenRepository.save(verificationToken);
	    return token;
	}
	@Autowired
	private JavaMailSender mailSender;

	public void sendVerificationEmail(String toEmail, String token) {
	    String url = "http://localhost:8080/verify?token=" + token;

	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(toEmail);
	    message.setSubject("Email Verification");
	    message.setText("Click this link to verify your email: " + url);

	    mailSender.send(message);
	}


}

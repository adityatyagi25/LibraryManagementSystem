package com.librarymanagementsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.librarymanagementsystem.Entity.VerificationToken;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {
	VerificationToken findByToken(String token);

	void deleteByUser_Email(String email);

	Optional<VerificationToken> findByUser_Email(String email);
}

package com.librarymanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.librarymanagementsystem.Entity.VerificationToken;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {
	VerificationToken findByToken(String token);
}

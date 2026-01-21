package com.librarymanagementsystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.Repository.BooksRepository;
import com.librarymanagementsystem.Repository.UsersRepository;

@Service
public class ReportService {
	@Autowired
	private BooksRepository bookRepository; 
	@Autowired
	private UsersRepository userRepository;

	public ResponseEntity<Integer> totalBooks() {
		return new ResponseEntity<>(bookRepository.getTotalBooks(),HttpStatus.OK);
	}

	public ResponseEntity<Integer> availableBooks() {
		return new ResponseEntity<>(bookRepository.getAvailableBooks(),HttpStatus.OK);
	}

	public ResponseEntity<Integer> issuedBooks() {
		return new ResponseEntity<>(bookRepository.getTotalBooks()-bookRepository.getAvailableBooks(),HttpStatus.OK);
	}

	public ResponseEntity<Integer> verifiedUsers() {
		return new ResponseEntity<>(userRepository.countByIsVerifiedTrue(),HttpStatus.OK);
	}

	public ResponseEntity<Integer> unverifiedUsers() {
		return new ResponseEntity<>(userRepository.countByIsVerifiedFalse(),HttpStatus.OK);
	}

	

}

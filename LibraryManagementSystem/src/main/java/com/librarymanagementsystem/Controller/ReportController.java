package com.librarymanagementsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.Service.ReportService;

@RestController
public class ReportController {
	@Autowired
	private ReportService reportService;
	@GetMapping("/TotalBooks")
	public ResponseEntity<Integer> totalBooks(){
		return reportService.totalBooks();	
	}
	@GetMapping("/AvailableBooks")
	public ResponseEntity<Integer> availableBooks(){
		return reportService.availableBooks();
	}
	@GetMapping("/IssuedBooks")
	public ResponseEntity<Integer> issuedBooks(){
		return reportService.issuedBooks();
	}
    @GetMapping("/VerifiedUsers")
	public ResponseEntity<Integer> verifiedUsers(){
    	return reportService.verifiedUsers();
	}
	@GetMapping("/UnverifiedUsers")
	public ResponseEntity<Integer> unverifiedUsers(){
		return reportService.unverifiedUsers();
	}
	
}

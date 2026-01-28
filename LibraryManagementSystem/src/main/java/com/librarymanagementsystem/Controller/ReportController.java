package com.librarymanagementsystem.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.BorrowRecordsDTO;
import com.librarymanagementsystem.Service.ReportService;

@RestController
public class ReportController {
	@Autowired
	private ReportService reportService;
	@GetMapping("/totalBooks")
	public ResponseEntity<Integer> totalBooks(){
		return reportService.totalBooks();	
	}
	@GetMapping("/availableBooks")
	public ResponseEntity<Integer> availableBooks(){
		return reportService.availableBooks();
	}
	@GetMapping("/issuedBooks")
	public ResponseEntity<Integer> issuedBooks(){
		return reportService.issuedBooks();
	}
    @GetMapping("/verifiedUsers")
	public ResponseEntity<Integer> verifiedUsers(){
    	return reportService.verifiedUsers();
	}
	@GetMapping("/unverifiedUsers")
	public ResponseEntity<Integer> unverifiedUsers(){
		return reportService.unverifiedUsers();
	}
	@GetMapping("/booksIssuedBetween")
	public ResponseEntity<List<BorrowRecordsDTO>> booksIssuedBetween(
	        @RequestParam LocalDate startDate,
	        @RequestParam LocalDate endDate) {
	    return reportService.booksIssuedBetween(startDate, endDate);
	}

	@GetMapping("/totalFineCollected")
	public ResponseEntity<Long> totalFineCollected(){
		return reportService.totalFineCollected();
	}
	@GetMapping("/pendingFine")
	public ResponseEntity<Long> pendingFine(){
		return reportService.pendingFine();
	}
	@GetMapping("/bookIssuedBy/{id}")
	public ResponseEntity<List<BorrowRecordsDTO>> bookIssuedBy(@PathVariable String id){
		return reportService.booksIssuedBy(id);
	}
	@GetMapping("/finePerUser/{id}")
	public ResponseEntity<Long> finePerUser(@PathVariable String id){
		return reportService.finePerUser(id);
	}
	
}

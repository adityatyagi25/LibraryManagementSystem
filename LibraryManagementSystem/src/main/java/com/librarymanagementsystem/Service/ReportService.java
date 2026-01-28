package com.librarymanagementsystem.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.DTO.BorrowRecordsDTO;
import com.librarymanagementsystem.Entity.BorrowRecords;
import com.librarymanagementsystem.Mapper.BorrowRecordsMapper;
import com.librarymanagementsystem.Repository.BooksRepository;
import com.librarymanagementsystem.Repository.BorrowRecordsRepository;
import com.librarymanagementsystem.Repository.UsersRepository;

@Service
public class ReportService {
	@Autowired
	private BooksRepository bookRepository; 
	@Autowired
	private UsersRepository userRepository;
	@Autowired
	private BorrowRecordsRepository borrowRecordsRepository;

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

	public ResponseEntity<List<BorrowRecordsDTO>> booksIssuedBetween(LocalDate startDate,LocalDate endDate) {
	
		    List<BorrowRecords> records =
		            borrowRecordsRepository.findByBorrowDateBetween(startDate, endDate);
		   return new ResponseEntity<>(records.stream()
	                .map(BorrowRecordsMapper::toDto)
	                .collect(Collectors.toList()),HttpStatus.OK);
		    
	}
     public ResponseEntity<Long> totalFineCollected() {
    		return new ResponseEntity<>(borrowRecordsRepository.getTotalFineCollected(),HttpStatus.OK);
	
	}

	public ResponseEntity<Long> pendingFine() {
	     	return new ResponseEntity<>(borrowRecordsRepository.pendingFine(),HttpStatus.OK);
	}

	public ResponseEntity<List<BorrowRecordsDTO>> booksIssuedBy(String id) {
		 List<BorrowRecordsDTO> dtoList =
		            borrowRecordsRepository.booksIssuedBy(id)
		                    .stream()
		                    .map(BorrowRecordsMapper::toDto)
		                    .toList();

		    return ResponseEntity.ok(dtoList);
	}

	public ResponseEntity<Long> finePerUser(String id) {
		return new ResponseEntity<>(borrowRecordsRepository.finePerUser(id),HttpStatus.OK);
	}
    

	

}

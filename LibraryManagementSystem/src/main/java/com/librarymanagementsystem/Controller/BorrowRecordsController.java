package com.librarymanagementsystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.BorrowRecordsDTO;
import com.librarymanagementsystem.DTO.BorrowRecordsDTO2;
import com.librarymanagementsystem.Service.BorrowRecordsService;
@RestController
public class BorrowRecordsController {
	@Autowired
	private BorrowRecordsService borrowRecordsService;
	@GetMapping("/findAllRecords")
	public List<BorrowRecordsDTO> getAllRecords() {
		return borrowRecordsService.getAllRecords();
	}
	@GetMapping("/findRecordsById/{id}")
	public BorrowRecordsDTO getRecordById(@PathVariable long id) {
	   return borrowRecordsService.getRecordsById(id);
	}
	@PostMapping("/borrowBook")
	public ResponseEntity<String> borrowBook(@RequestBody BorrowRecordsDTO2 borrowRecords) {
		return borrowRecordsService.borrowBook(borrowRecords);
	}
	@PatchMapping("/returnBook/{id}")
	public ResponseEntity<String> returnBook(@PathVariable long id) {
	    return borrowRecordsService.returnBook(id);
	}
}

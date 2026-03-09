package com.librarymanagementsystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.BorrowRecordsDTO;
import com.librarymanagementsystem.DTO.BorrowRecordsDTO2;
import com.librarymanagementsystem.DTO.MostLeastBorrowedBooksDTO;
import com.librarymanagementsystem.DTO.UserCategoryDTO;
import com.librarymanagementsystem.Service.BorrowRecordsService;

@RestController
@RequestMapping("/librarian")
public class BorrowRecordsController {

	@Autowired
	private BorrowRecordsService borrowRecordsService;

	@GetMapping("/findRecordsById/{id}")
	public BorrowRecordsDTO getRecordById(@PathVariable long id) {
		return borrowRecordsService.getRecordsById(id);
	}

	@PostMapping("/borrowBook")
	public ResponseEntity<String> borrowBook(@RequestBody BorrowRecordsDTO2 borrowRecords) {
		return borrowRecordsService.borrowBook(borrowRecords);
	}

	@GetMapping("/findAllRecords")
	public Page<BorrowRecordsDTO> getAllBorrowRecords(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "borrowId") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		return borrowRecordsService.getAllRecordsPaginated(page, size, sortBy, direction);
	}
	
	
	// Not Tested
	@GetMapping("/findRecordsByUser/{userId}")
	public Page<BorrowRecordsDTO> findRecordsByUser(
	        @PathVariable String userId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "borrowId") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction) {

	    return borrowRecordsService.findRecordsByUser(userId, page, size, sortBy, direction);
	}
    
    //Not Tested
	@GetMapping("/findRecordsByBook/{bookId}")
	public Page<BorrowRecordsDTO> findRecordsByBook(
	        @PathVariable long bookId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "borrowId") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction) {

	    return borrowRecordsService.findRecordsByBook(bookId, page, size, sortBy, direction);
	}
    
	
	
	@PatchMapping("/returnBook/{id}")
	public ResponseEntity<String> returnBook(@PathVariable long id) {
		return borrowRecordsService.returnBook(id);
	}

	@PatchMapping("/payFine/{id}/{amount}")
	public ResponseEntity<String> payFine(@PathVariable long id, Long amount) {
		return borrowRecordsService.payFine(id, amount);

	}
	
	@GetMapping("/intrestedCategory")
	public List<UserCategoryDTO> intrestedCategory(@RequestParam String email){
		return borrowRecordsService.intrestedCategory(email);
	}
	
	@GetMapping("/favouriteCategoryOfUser/{email}")
	public String favouriteCategory(@PathVariable String email) {
		return borrowRecordsService.favouriteCategory(email);
	}
    @GetMapping("/mostBorrowedBook")
    public MostLeastBorrowedBooksDTO mostBorrowedBook() {
    	return borrowRecordsService.mostBorrowedBook();
    	
    }
    @GetMapping("/leastBorrowedBook")
    public MostLeastBorrowedBooksDTO leastborrowedbook() {
    	return borrowRecordsService.leastBorrowedBook();
    }
	
}

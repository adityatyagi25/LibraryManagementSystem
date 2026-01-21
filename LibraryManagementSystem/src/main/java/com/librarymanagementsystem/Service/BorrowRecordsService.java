package com.librarymanagementsystem.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.DTO.BorrowRecordsDTO;
import com.librarymanagementsystem.DTO.BorrowRecordsDTO2;
import com.librarymanagementsystem.Entity.Books;
import com.librarymanagementsystem.Entity.BorrowRecords;
import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Exception.NoRecordFoundException;
import com.librarymanagementsystem.Mapper.BorrowRecordsMapper;
import com.librarymanagementsystem.Repository.BooksRepository;
import com.librarymanagementsystem.Repository.BorrowRecordsRepository;
import com.librarymanagementsystem.Repository.UsersRepository;

@Service
public class BorrowRecordsService {
	
    private static final int MAX_PAGE_SIZE = 20;
	@Autowired
	private BooksRepository booksRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private BorrowRecordsRepository borrowRecordsRepository;

//	public List<BorrowRecordsDTO> getAllRecords() {
//		return borrowRecordsRepository.findAll().stream().map(BorrowRecordsMapper::toDto).toList();
//	}

	public BorrowRecordsDTO getRecordsById(long id) {
		return BorrowRecordsMapper.toDto(borrowRecordsRepository.findById(id)
				.orElseThrow(() -> new NoRecordFoundException("No record Found with id: " + id)));
	}
	
	
	
	private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
	            "borrowId",
	            "borrowDate",
	            "dueDate",
	            "returnDate",
	            "fineAmount"
	    );
	    public Page<BorrowRecordsDTO> getAllRecordsPaginated(
	            int page,
	            int size,
	            String sortBy,
	            String direction
	    ) {

	        if (page < 0) {
	           page=0;
	        }

	        if (size <= 0 || size > MAX_PAGE_SIZE) {
	          size=10;
	        }

	        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
	        
	            sortBy = "borrowId"; 
	        }

	        Sort sort = "desc".equalsIgnoreCase(direction)
	                ? Sort.by(sortBy).descending()
	                : Sort.by(sortBy).ascending();

	        Pageable pageable = PageRequest.of(page, size, sort);

	        return borrowRecordsRepository.findAll(pageable)
	                .map(BorrowRecordsMapper::toDto);
	    }
	
	
	
	// Untested Code    
	
	
	public ResponseEntity<String> borrowBook(BorrowRecordsDTO2 borrowRecords) {
		Optional<Books> existingBok = booksRepository.findByTitle(borrowRecords.getBookName());
		if (existingBok.isEmpty()) {
			return new ResponseEntity<>("Book not found!! ", HttpStatus.OK);
		}
		Books existingBook = existingBok.get();
		Optional<Users> existingUsr = usersRepository.findById(borrowRecords.getUserEmail());
		if (existingUsr.isEmpty()) {
			return new ResponseEntity<>("User not found!! ", HttpStatus.OK);
		}
		Users existingUser = existingUsr.get();
		if (existingBook.getAvailableCopies() <= 0) {
			return new ResponseEntity<>("There are no available copies of the book available", HttpStatus.OK);
		}
		if(existingUser.isVerified()==false) {
			return new ResponseEntity<>("User is not Verified",HttpStatus.OK);
		}
		
		
		int availableCopies = existingBook.getAvailableCopies();
		existingBook.setAvailableCopies(availableCopies - 1);
		booksRepository.save(existingBook);
		BorrowRecords br = new BorrowRecords();
		br.setBorrowDate(LocalDate.now());
		br.setDueDate(LocalDate.now().plusDays(14));
		br.setBook(existingBook);
		br.setUser(existingUser);
		borrowRecordsRepository.save(br);

		return new ResponseEntity<>("Book Borrowed", HttpStatus.OK);
	}

	public ResponseEntity<String> returnBook(long id) {
		Optional<BorrowRecords> borrowRecords = borrowRecordsRepository.findById(id);
		if(borrowRecords.isEmpty()) {
			return new ResponseEntity<>("No Record Found !!", HttpStatus.OK);
		}
		BorrowRecords br=borrowRecords.get();
		if (br.getReturnDate() == null) {
			br.setReturnDate(LocalDate.now());
			// fine calculation
			// 15 December code
			// By Mrr.ADDY
			int daysBetween = (int) ChronoUnit.DAYS.between(br.getBorrowDate(), LocalDate.now());
			int difference = daysBetween - 14;
			if (difference >= 1) {
				br.setFineAmount(difference * 50);
			}
			//If fine amount == 0 , Boolean -> true
			if(br.getFineAmount()<=0) {
				br.setFineReturned(true);
			}
			Books book = br.getBook();
			int availableCopies = book.getAvailableCopies();
			book.setAvailableCopies(availableCopies + 1);
			booksRepository.save(book);
			borrowRecordsRepository.save(br);

			return new ResponseEntity<>("Book Returned", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Book Already Returned", HttpStatus.OK);
		}
	}

	public ResponseEntity<String> payFine(long id, int amount) {
		Optional<BorrowRecords> borrowRecords = borrowRecordsRepository.findById(id);
		if(borrowRecords.isEmpty()) {
			return new ResponseEntity<>("No Record Found !!", HttpStatus.OK);
		}
		BorrowRecords br=borrowRecords.get();
		int amountLeft=br.getFineAmount()-amount;
		if(amountLeft<0) {
			return new ResponseEntity<>("You are returning amount more than fine !! Your fine amount is "+br.getFineAmount(),HttpStatus.OK);
		}
		// Boolean (is fine returned) Not Tested
		br.setFineAmount(amountLeft);
		if(amountLeft<=0) {
			br.setFineReturned(true);
		}
		borrowRecordsRepository.save(br);
		return new ResponseEntity<>("now fine left is "+amountLeft,HttpStatus.OK);
		
		
	}

}

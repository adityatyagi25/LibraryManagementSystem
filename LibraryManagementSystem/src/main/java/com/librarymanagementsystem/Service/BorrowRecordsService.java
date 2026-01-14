package com.librarymanagementsystem.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private BooksRepository booksRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private BorrowRecordsRepository borrowRecordsRepository;

	public List<BorrowRecordsDTO> getAllRecords() {
		return borrowRecordsRepository.findAll().stream().map(BorrowRecordsMapper::toDto).toList();
	}

	public BorrowRecordsDTO getRecordsById(long id) {
		return BorrowRecordsMapper.toDto(borrowRecordsRepository.findById(id)
				.orElseThrow(() -> new NoRecordFoundException("No record Found with id: " + id)));
	}

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
		br.setBorrow_date(LocalDate.now());
		br.setDue_date(LocalDate.now().plusDays(14));
		br.setBook(existingBook);
		br.setUser(existingUser);
		borrowRecordsRepository.save(br);

		return new ResponseEntity<>("Book Borrowed", HttpStatus.OK);
	}

	public ResponseEntity<String> returnBook(long id) {
		BorrowRecords br = borrowRecordsRepository.findById(id).orElseThrow();
		if (br.getReturn_date() == null) {
			br.setReturn_date(LocalDate.now());
			// fine calculation
			// 15 December code
			// By Mrr.ADDY
			int daysBetween = (int) ChronoUnit.DAYS.between(br.getBorrow_date(), LocalDate.now());
			int difference = daysBetween - 14;
			if (difference >= 1) {
				br.setFine_amount(difference * 50);
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
		BorrowRecords br = borrowRecordsRepository.findById(id).orElseThrow();
		int amountLeft=br.getFine_amount()-amount;
		if(amountLeft<0) {
			return new ResponseEntity<>("You are returning amount more than fine !! Your fine amount is "+br.getFine_amount(),HttpStatus.OK);
		}
		br.setFine_amount(amountLeft);
		borrowRecordsRepository.save(br);
		return new ResponseEntity<>("now fine left is "+amountLeft,HttpStatus.OK);
		
	}

}

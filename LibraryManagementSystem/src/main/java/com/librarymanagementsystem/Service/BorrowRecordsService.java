package com.librarymanagementsystem.Service;

import java.time.LocalDate;
import java.util.List;
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
import com.librarymanagementsystem.Exception.BookNotFoundException;
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
		 return BorrowRecordsMapper.toDto(borrowRecordsRepository.findById(id).orElseThrow(()->new NoRecordFoundException("No record Found with id: "+id)));
	}
	public ResponseEntity<String> borrowBook(BorrowRecordsDTO2 borrowRecords) {
		   Books existingBook = booksRepository.findByTitle(borrowRecords.getBookName())
			    .orElseThrow(() -> new RuntimeException("Book not found"));

		   Users existingUser = usersRepository.findById(borrowRecords.getUserEmail())
				   .orElseThrow(() -> new RuntimeException("User not found"));  
		   if(existingBook.getAvailableCopies()==0||existingBook.getAvailableCopies()<0) {
			   throw new BookNotFoundException("There are no available copies of the book available");
		   }
		   int availableCopies=existingBook.getAvailableCopies();
		   existingBook.setAvailableCopies(availableCopies-1);
		   booksRepository.save(existingBook);
		BorrowRecords br=new BorrowRecords();
		br.setBorrow_date(LocalDate.now());
		br.setDue_date(LocalDate.now().plusDays(14));
		br.setBook(existingBook);
		br.setUser(existingUser);
	    borrowRecordsRepository.save(br);
	    
	    return new ResponseEntity<>("Book Borrowed",HttpStatus.OK);
	}
	public ResponseEntity<String> returnBook(long id) {
		BorrowRecords br=borrowRecordsRepository.findById(id).orElseThrow();
		if(br.getReturn_date()==null) {
		br.setReturn_date(LocalDate.now());
		Books book=br.getBook();
		  int availableCopies=book.getAvailableCopies();
		  book.setAvailableCopies(availableCopies+1);
		  booksRepository.save(book);
		borrowRecordsRepository.save(br);
		return new ResponseEntity<>("Book Returned",HttpStatus.OK);
		}
		else {
		return new ResponseEntity<>("Book Already Returned",HttpStatus.BAD_REQUEST);
		}
	}

}

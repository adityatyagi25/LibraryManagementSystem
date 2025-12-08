package com.librarymanagementsystem.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.DTO.BooksDTO;
import com.librarymanagementsystem.Entity.Books;
import com.librarymanagementsystem.Entity.Categories;
import com.librarymanagementsystem.Exception.BookNotFoundException;
import com.librarymanagementsystem.Repository.BooksRepository;
import com.librarymanagementsystem.Repository.CategoriesRepository;

@Service
public class BooksService {
	@Autowired
	private BooksRepository booksRepository;
	@Autowired
	private CategoriesRepository categoriesRepository;

	public ResponseEntity<String> addBook(BooksDTO bookDTO) {
		Optional<Books> checkBook=booksRepository.findByTitle(bookDTO.getTitle());
		if(checkBook.isEmpty()) {
		Books book=new Books();
		book.setAuthor(bookDTO.getAuthor());
		book.setAvailableCopies(bookDTO.getTotalCopies());
		book.setTotalCopies(bookDTO.getTotalCopies());
		 Categories category = categoriesRepository
		            .findByCategoryName(bookDTO.getCategory())
		            .orElseThrow(() -> new RuntimeException("Category not found"));
		 book.setCategory(category);

		book.setIsbn(bookDTO.getIsbn());
		book.setTitle(bookDTO.getTitle());
		booksRepository.save(book);
		return new ResponseEntity<>("Book Saved",HttpStatus.OK);
		}
		else {
		return new ResponseEntity<>("Book Already Exists",HttpStatus.CONFLICT);	
		}
	}

	public ResponseEntity<String> updateBook(Books book, long id) {
		Books updatedBooks = booksRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException("No book Found with id " + id ));
		updatedBooks.setAuthor(book.getAuthor());
		updatedBooks.setTitle(book.getTitle());
		updatedBooks.setIsbn(book.getIsbn());
		updatedBooks.setTotalCopies(book.getTotalCopies());
		booksRepository.save(updatedBooks);
		return new ResponseEntity<>("Book Updated",HttpStatus.OK);
	}

	public ResponseEntity<String> deleteBook(long id) {
		booksRepository.findById(id)
		.orElseThrow(() -> new BookNotFoundException("No book Found with id " + id ));
		booksRepository.deleteById(id);	
		return new ResponseEntity<>("Book Deleted",HttpStatus.OK);
	}

	public ResponseEntity<Books> findBookById(long id) {
		Books book=booksRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException("No book Found with id " + id));
		return new ResponseEntity<>(book,HttpStatus.OK);	
	}

	public List<Books> findAll() {
		return booksRepository.findAll();
	}

	public ResponseEntity<?> findBookByCategory(String category) {
		Optional<Categories> var = categoriesRepository.findByCategoryName(category);
		if(var.isPresent()) {
		return new ResponseEntity<>(booksRepository.findAllByCategory(var.get()),HttpStatus.OK);
		}
		else {
		return new ResponseEntity<>("No Category Found",HttpStatus.BAD_REQUEST);
		}

	}
	

	 
}

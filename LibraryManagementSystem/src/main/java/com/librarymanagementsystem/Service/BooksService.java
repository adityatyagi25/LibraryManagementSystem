package com.librarymanagementsystem.Service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.DTO.BooksDTO;
import com.librarymanagementsystem.Entity.Books;
import com.librarymanagementsystem.Entity.Categories;
import com.librarymanagementsystem.Repository.BooksRepository;
import com.librarymanagementsystem.Repository.CategoriesRepository;

@Service
public class BooksService {

	private static final int MAX_PAGE_SIZE = 20;
	@Autowired
	private BooksRepository booksRepository;
	@Autowired
	private CategoriesRepository categoriesRepository;
	private static final Logger logger = LoggerFactory.getLogger(BooksService.class);

	public ResponseEntity<String> addBook(BooksDTO bookDTO) {
	    String regex="^\\d{12}$";
		if (bookDTO.getAuthor().length() > 100 || bookDTO.getIsbn().length() > 100
				|| bookDTO.getTitle().length() > 100) {
			logger.warn("Please use characters within range");
			return new ResponseEntity<>("Please use characters less than 100", HttpStatus.OK);
		}
		if(!bookDTO.getIsbn().matches(regex)) {
			logger.warn("Isbn number should excatly contains 12 characters");
			return new ResponseEntity<>("Isbn number should exactly contains 12 characters",HttpStatus.OK);
		}
     
		if (bookDTO.getTotalCopies() < 1) {
			logger.warn("The total copies should be more than 0");
			return new ResponseEntity<>("The total copies should be more than 0", HttpStatus.OK);
		}
		
		Optional<Books> checkisbn = booksRepository.findByIsbn(bookDTO.getIsbn());
		if (checkisbn.isPresent()) {
			logger.warn("Book with same Isbn already Present");
			return new ResponseEntity<>("Book with same Isbn already Present", HttpStatus.OK);
		}

		Optional<Books> checkBook = booksRepository.findByTitle(bookDTO.getTitle());
		if (checkBook.isEmpty()) {
			Books book = new Books();
			book.setAuthor(bookDTO.getAuthor());
			book.setAvailableCopies(bookDTO.getTotalCopies());
			book.setTotalCopies(bookDTO.getTotalCopies());
			Optional<Categories> category = categoriesRepository.findByCategoryName(bookDTO.getCategory());
			if (category.isEmpty()) {
				return new ResponseEntity<>("Category Not Found", HttpStatus.OK);
			}
			Categories categoryy = category.get();
			book.setCategory(categoryy);

			book.setIsbn(bookDTO.getIsbn());
			book.setTitle(bookDTO.getTitle());
			booksRepository.save(book);
			return new ResponseEntity<>("Book Saved", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Book with same Title already Exists", HttpStatus.OK);
		}
	}

	public ResponseEntity<String> deleteBook(long id) {
		Optional<Books> book = booksRepository.findById(id);
		if (book.isEmpty()) {
			return new ResponseEntity<>("Book Not Found", HttpStatus.OK);
		}

		booksRepository.deleteById(id);
		return new ResponseEntity<>("Book Deleted", HttpStatus.OK);
	}

	public ResponseEntity<?> findBookById(long id) {
		Optional<Books> book = booksRepository.findById(id);
		if (book.isEmpty()) {
			return new ResponseEntity<>("No Book found with id" + id, HttpStatus.OK);
		}
		Books books = book.get();
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("bookId", "title", "author", "isbn", "totalCopies",
			"availableCopies");

	public Page<Books> findAllBooks(int page, int size, String sortBy, String sortDir) {

		if (page < 0) {
			logger.warn("Page choosen is < 0 , So using default");
			page = 0;
		}

		
		if (size <= 0 || size > MAX_PAGE_SIZE) {
			logger.warn("Size choosen is < 0 or More than MAX_PAGE_SIZE, So using default");
			size = 10;
		}

		if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
			logger.warn("Size choosen is < 0 or More than MAX_PAGE_SIZE, So using default");
			sortBy = "bookId"; 
		}

		Sort sort = "desc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		return booksRepository.findAll(pageable);
	}

	public ResponseEntity<?> findBookByCategory(String category, int page, int size, String sortBy, String sortDir) {
        
		if (category.length() > 100) {
			return new ResponseEntity<>("Please use characters less than 100", HttpStatus.OK);
		}
		Optional<Categories> optionalCategory = categoriesRepository.findByCategoryName(category);
         
		if (optionalCategory.isEmpty()) {
			return new ResponseEntity<>("No Category Found with name: " + category, HttpStatus.OK);
		}

		if (page < 0) {
			logger.warn("Page choosen is < 0 , So using default");
			page = 0;
		}

		if (size <= 0 || size > MAX_PAGE_SIZE) {
			logger.warn("Size choosen is < 0 or More than MAX_PAGE_SIZE, So using default");
			size = 10;
		}

		if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {
			logger.warn("Size choosen is < 0 or More than MAX_PAGE_SIZE, So using default");
			sortBy = "bookId";
		}

		Sort sort = Sort.by(sortBy);
		if ("desc".equalsIgnoreCase(sortDir)) {
			sort = sort.descending();
		} else {
			sort = sort.ascending();
		}

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Books> booksPage = booksRepository.findAllByCategory(optionalCategory.get(), pageable);

		return new ResponseEntity<>(booksPage, HttpStatus.OK);
	}

	public ResponseEntity<?> findBookByAuthor(String author , int page, int size, String sortBy, String sortDir) {
		if (author.length() > 100) {
			return new ResponseEntity<>("Please use characters less than 100", HttpStatus.OK);
		}
		if (page < 0) {
			logger.warn("Page choosen is < 0 , So using default");
			page = 0;
		}
		if (size <= 0 || size > MAX_PAGE_SIZE) {
			logger.warn("Size choosen is < 0 or More than MAX_PAGE_SIZE, So using default");
			size = 10;
		}
		if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {
			logger.warn("Size choosen is < 0 or More than MAX_PAGE_SIZE, So using default");
			sortBy = "bookId";
		}
		Sort sort = Sort.by(sortBy);
		if ("desc".equalsIgnoreCase(sortDir)) {
			sort = sort.descending();
		} else {
			sort = sort.ascending();
		}

		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<Books> booksPage = booksRepository.findAllByAuthor(author, pageable);
		return new ResponseEntity<>(booksPage, HttpStatus.OK);
		
	}

}

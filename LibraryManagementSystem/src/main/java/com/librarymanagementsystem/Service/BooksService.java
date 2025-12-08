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
		if(bookDTO.getTotalCopies()<1) {
			return new ResponseEntity<>("The total copies should be more than 0",HttpStatus.OK);
		}
        Optional<Books> checkisbn=booksRepository.findByIsbn(bookDTO.getIsbn());
        if(checkisbn.isPresent()) {
        	return new ResponseEntity<>("Book with same Isbn already Present",HttpStatus.OK);
        }
		
		Optional<Books> checkBook=booksRepository.findByTitle(bookDTO.getTitle());
		if(checkBook.isEmpty()) {
		Books book=new Books();
		book.setAuthor(bookDTO.getAuthor());
		book.setAvailableCopies(bookDTO.getTotalCopies());
		book.setTotalCopies(bookDTO.getTotalCopies());
		 Optional<Categories> category = categoriesRepository
		            .findByCategoryName(bookDTO.getCategory());
		 if(category.isEmpty()) {
			 return new ResponseEntity<>("Category Not Found",HttpStatus.OK);
		 }
		 Categories categoryy=category.get();
		 book.setCategory(categoryy);

		book.setIsbn(bookDTO.getIsbn());
		book.setTitle(bookDTO.getTitle());
		booksRepository.save(book);
		return new ResponseEntity<>("Book Saved",HttpStatus.OK);
		}
		else {
		return new ResponseEntity<>("Book Already Exists",HttpStatus.OK);	
		}
	}
	public ResponseEntity<String> deleteBook(long id) {
	  Optional<Books> book=	booksRepository.findById(id);
	  if(book.isEmpty()) {
		 return new ResponseEntity<>("Book Not Found",HttpStatus.OK);
	  }
	  
		booksRepository.deleteById(id);	
		return new ResponseEntity<>("Book Deleted",HttpStatus.OK);
	}

	public ResponseEntity<?> findBookById(long id) {
		Optional<Books> book=booksRepository.findById(id);
		if(book.isEmpty()) {
			return new ResponseEntity<>("No Book found with id"+id,HttpStatus.OK);
		}
		Books books=book.get();
		return new ResponseEntity<>(books,HttpStatus.OK);	
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
		return new ResponseEntity<>("No Category Found",HttpStatus.OK);
		}

	}
}

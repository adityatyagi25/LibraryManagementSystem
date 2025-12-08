package com.librarymanagementsystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.BooksDTO;
import com.librarymanagementsystem.Entity.Books;
import com.librarymanagementsystem.Service.BooksService;
@RestController
public class BooksController {
	@Autowired
    private BooksService booksService;
	@PostMapping("/addBook")// this is to add book
	public ResponseEntity<String> addBook(@RequestBody BooksDTO book) {
		if(book.getTotalCopies()<0) {
			throw new RuntimeException("The total copies should be more than 0");
		}
		return booksService.addBook(book);
	}
    @PutMapping("/updateBook/{id}")
	public ResponseEntity<String> updateBook(@RequestBody Books book,@PathVariable long id ) {
		return booksService.updateBook(book,id);
	} 
    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable long id) {
    	return booksService.deleteBook(id); 	
    }
    @GetMapping("/findBookById/{id}")
    public ResponseEntity<Books> findBookById(@PathVariable long id) {
    	return booksService.findBookById(id);
    }
    @GetMapping("/findAllBooks")
    public List<Books> findAllBooks(){
    	return booksService.findAll();
    }
    @GetMapping("/findBooksByCategory/{category}")
    public ResponseEntity<?> findBooksByCategory(@PathVariable String category){
    	return booksService.findBookByCategory(category);
    }
}

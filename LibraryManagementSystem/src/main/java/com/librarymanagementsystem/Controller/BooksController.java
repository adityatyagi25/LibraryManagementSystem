package com.librarymanagementsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.BooksDTO;
import com.librarymanagementsystem.Entity.Books;
import com.librarymanagementsystem.Service.BooksService;
@RestController
@RequestMapping("/librarian")
public class BooksController {
	@Autowired
    private BooksService booksService;
	@PostMapping("/addBook")
	public ResponseEntity<String> addBook(@RequestBody BooksDTO book) {
		return booksService.addBook(book);
	}
    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable long id) {
    	return booksService.deleteBook(id); 	
    }
    @GetMapping("/findBookById/{id}")
    public ResponseEntity<?> findBookById(@PathVariable long id) {
    	return booksService.findBookById(id);
    }
    @GetMapping("/findAllBooks")
    public ResponseEntity<Page<Books>> findAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(
                booksService.findAllBooks(page, size, sortBy, sortDir)
        );
    }

    @GetMapping("/findBooksByCategory/{category}")
    public ResponseEntity<?> findBooksByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return booksService.findBookByCategory(category, page, size, sortBy, sortDir);
    }
    @GetMapping("/findBooksByAuthor/{author}")
    public ResponseEntity<?> findBooksByAuthor(
            @PathVariable String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return booksService.findBookByAuthor(author, page, size, sortBy, sortDir);
    }
}

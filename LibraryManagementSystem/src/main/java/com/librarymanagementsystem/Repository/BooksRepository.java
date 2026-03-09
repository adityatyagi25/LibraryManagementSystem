package com.librarymanagementsystem.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.librarymanagementsystem.Entity.Books;
import com.librarymanagementsystem.Entity.Categories;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

	Optional<Books> findByTitle(String bookName);

	Optional<Books> findByIsbn(String isbn);

	Page<Books> findAllByCategoryAndStatusTrue(Categories category, Pageable pageable);

	@Query(value = "SELECT SUM(total_copies) FROM books", nativeQuery = true)
	Integer getTotalBooks();

	Page<Books> findAllByAuthorAndStatusTrue(String author, Pageable pageable);

	@Query(value = "SELECT SUM(available_copies) FROM books", nativeQuery = true)
	Integer getAvailableBooks();
	
    Page<Books> findByStatusTrue(Pageable pageable);
}

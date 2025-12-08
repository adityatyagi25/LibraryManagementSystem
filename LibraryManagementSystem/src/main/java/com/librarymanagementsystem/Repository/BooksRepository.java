package com.librarymanagementsystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagementsystem.Entity.Books;
import com.librarymanagementsystem.Entity.Categories;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
	List<Books> findAllByCategory(Categories category);
	Optional<Books> findByTitle(String bookName);
	Optional<Books> findByIsbn(String isbn);

}

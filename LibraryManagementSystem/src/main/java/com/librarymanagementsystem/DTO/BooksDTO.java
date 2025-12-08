package com.librarymanagementsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksDTO {
	private String title;

	private String author;

	private String isbn;
    
    private int totalCopies;
    
    private String category;
}

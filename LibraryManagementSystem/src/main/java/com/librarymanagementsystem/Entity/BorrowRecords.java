package com.librarymanagementsystem.Entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecords {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long borrow_id;
	private LocalDate borrow_date;
	private LocalDate due_date;
	private LocalDate return_date;
    @JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Books book;
    @JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private Users user;
    private int fine_amount=0;
}

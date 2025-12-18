package com.librarymanagementsystem.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecordsDTO {
	private long borrow_id;
	private LocalDate borrow_date;
	private LocalDate due_date;
	private LocalDate return_date;
	private long bookId;
	private String bookName;
	private String userName;
	private int fine_amount;
}

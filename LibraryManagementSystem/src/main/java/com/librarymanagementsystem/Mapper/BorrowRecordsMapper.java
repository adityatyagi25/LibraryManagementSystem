package com.librarymanagementsystem.Mapper;

import com.librarymanagementsystem.DTO.BorrowRecordsDTO;
import com.librarymanagementsystem.Entity.BorrowRecords;

public class BorrowRecordsMapper {
	public static BorrowRecordsDTO toDto(BorrowRecords record) {
		BorrowRecordsDTO borrowRecordsDto = new BorrowRecordsDTO();
		borrowRecordsDto.setBorrow_id(record.getBorrowId());
		borrowRecordsDto.setBorrow_date(record.getBorrowDate());
		borrowRecordsDto.setDue_date(record.getDueDate());
		borrowRecordsDto.setReturn_date(record.getReturnDate());
		borrowRecordsDto.setBookId(record.getBook().getBookId());
		borrowRecordsDto.setBookName(record.getBook().getTitle());
		borrowRecordsDto.setUserName(record.getUser().getEmail());
		return borrowRecordsDto;
	}

}

package com.librarymanagementsystem.Mapper;

import com.librarymanagementsystem.DTO.BorrowRecordsDTO;
import com.librarymanagementsystem.Entity.BorrowRecords;

public class BorrowRecordsMapper {
	public static BorrowRecordsDTO toDto(BorrowRecords record) {
		BorrowRecordsDTO borrowRecordsDto=new BorrowRecordsDTO();
		borrowRecordsDto.setBorrow_id(record.getBorrow_id());
		borrowRecordsDto.setBorrow_date(record.getBorrow_date());
		borrowRecordsDto.setDue_date(record.getDue_date());
		borrowRecordsDto.setReturn_date(record.getReturn_date());
		borrowRecordsDto.setBookId(record.getBook().getBookId());
		borrowRecordsDto.setBookName(record.getBook().getTitle());
		borrowRecordsDto.setUserName(record.getUser().getEmail());
		return borrowRecordsDto;
	}
	

}

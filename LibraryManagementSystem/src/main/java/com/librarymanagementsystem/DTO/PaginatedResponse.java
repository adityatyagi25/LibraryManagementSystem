package com.librarymanagementsystem.DTO;

import org.springframework.data.domain.Page;

public class PaginatedResponse<T> {

	private String message;
	private Page<T> data;

	public PaginatedResponse(String message, Page<T> data) {
		this.message = message;
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public Page<T> getData() {
		return data;
	}
}

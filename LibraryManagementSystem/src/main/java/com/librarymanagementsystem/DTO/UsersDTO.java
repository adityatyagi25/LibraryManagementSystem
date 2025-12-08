package com.librarymanagementsystem.DTO;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersDTO {
	@Email
	private String email;
	private String password;
    private String role;
}

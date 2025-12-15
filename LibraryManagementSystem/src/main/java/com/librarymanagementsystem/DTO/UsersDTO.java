package com.librarymanagementsystem.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersDTO {

	private String email;
	private String password;
    private List<String> roles;
}

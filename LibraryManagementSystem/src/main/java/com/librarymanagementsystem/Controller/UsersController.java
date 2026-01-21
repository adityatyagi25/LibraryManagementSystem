package com.librarymanagementsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.UsersDTO;
import com.librarymanagementsystem.Service.UsersService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class UsersController {
	@Autowired
	private UsersService usersService;

	@PostMapping("/addUser")
	public ResponseEntity<String> addUser(@Valid @RequestBody UsersDTO userDTO) {
		return usersService.addUser(userDTO);

	}

	@DeleteMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam String email) {
		return usersService.deleteUser(email);
	}
}

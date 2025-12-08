package com.librarymanagementsystem.Controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.UsersDTO;
import com.librarymanagementsystem.Entity.Roles;
import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Repository.RolesRepository;
import com.librarymanagementsystem.Service.UsersService;

@RestController
@RequestMapping("/admin")
public class UsersController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private RolesRepository rolesRepository;

	@PostMapping("/addUser")
	public ResponseEntity<String> addUser(@RequestBody UsersDTO userDTO) {
		 if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
		        throw new IllegalArgumentException("Password cannot be null or empty");
		   }
		 Users user=new Users();
		 user.setEmail(userDTO.getEmail());
		 user.setPassword(userDTO.getPassword());
		 Optional<Roles> roleEntity = rolesRepository.findByRole(userDTO.getRole());
	        Set<Roles> roles = new HashSet<>();
	        if (roleEntity.isPresent()) {
	            roles.add(roleEntity.get());
	        }
	        else {
	        	throw new RuntimeException("Please use proper roles ie. (USER , ADMIN or LIBRARIAN)");
	        }
	        user.setRoles(roles);
		return usersService.addUser(user);
		
	}
	@DeleteMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam String email) {
		return usersService.deleteUser(email);
	}
}

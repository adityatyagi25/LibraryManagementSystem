package com.librarymanagementsystem.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Repository.UsersRepository;

@Service
public class UsersService {
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	public ResponseEntity<String> addUser(Users user) {
		if(usersRepository.findById(user.getEmail()).isPresent()) {
		throw new RuntimeException("User Already Exists");	
		}
		Users newUser=new Users();
		newUser.setEmail(user.getEmail());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setRoles(user.getRoles());
		usersRepository.save(newUser);
		return new ResponseEntity<>("User Added Successfully",HttpStatus.OK);
	}
	public ResponseEntity<String> deleteUser(String email) {
	   Optional<Users> checkUser=usersRepository.findById(email);
	   if(checkUser.isPresent()) {
	   usersRepository.deleteById(email);
	   return new ResponseEntity<>("User Deleted Successfully",HttpStatus.OK);
	   }
	   else
	   {
	   return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);  
	   }
		
	}
}

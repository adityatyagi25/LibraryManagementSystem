package com.librarymanagementsystem.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.DTO.UsersDTO;
import com.librarymanagementsystem.Entity.Roles;
import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Repository.RolesRepository;
import com.librarymanagementsystem.Repository.UsersRepository;
import com.librarymanagementsystem.Repository.VerificationTokenRepo;

@Service
public class UsersService {
	@Autowired
	private VerificationTokenService verificationTokenService;
	@Autowired
	private RolesRepository rolesRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public ResponseEntity<String> addUser(UsersDTO userDTO) {
		 if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
		        return new ResponseEntity<>("Password can't be null or empty",HttpStatus.OK);
		  }
		 Users user=new Users();
		 user.setEmail(userDTO.getEmail());
		 user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		 Optional<Roles> roleEntity = rolesRepository.findByRole(userDTO.getRole());
	        Set<Roles> roles = new HashSet<>();
	        if (roleEntity.isPresent()) {
	            roles.add(roleEntity.get());
	        }
	        else {
	            return new ResponseEntity<>("Please use proper roles ie. (USER , ADMIN or LIBRARIAN)",HttpStatus.OK);
	        }
	        user.setRoles(roles);
		if(usersRepository.findById(user.getEmail()).isPresent()) {
        return new ResponseEntity<>("User Already Exists",HttpStatus.OK);
		
		}
		
		usersRepository.save(user);
		String token=verificationTokenService.generateVerificationToken(user);
		verificationTokenService.sendVerificationEmail(user.getEmail(), token);
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
       return new ResponseEntity<>("User Not Found",HttpStatus.OK);
	   }
		
	}
}

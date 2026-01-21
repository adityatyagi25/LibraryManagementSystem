package com.librarymanagementsystem.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.DTO.RolesDTO;
import com.librarymanagementsystem.Entity.Roles;
import com.librarymanagementsystem.Repository.RolesRepository;

@Service
public class RolesService {
	@Autowired
	private RolesRepository rolesRepository;

	public ResponseEntity<String> addRole(RolesDTO role) {
		if(role.getRole().length()>100) {
			return new ResponseEntity<>("Please use characters less than 100", HttpStatus.OK);
		}
		Optional<Roles> roles = rolesRepository.findByRole(role.getRole());
		if (role.getRole().length() > 20 || role.getRole().length() < 2) {
			return new ResponseEntity<>("Please use 2-20 characters... ", HttpStatus.OK);
		}
		if (roles.isPresent()) {
			return new ResponseEntity<>("Role Already Exists ", HttpStatus.OK);
		} else {
			Roles rolee = new Roles();
			rolee.setRole(role.getRole());
			rolesRepository.save(rolee);
			return new ResponseEntity<>("Role Added ", HttpStatus.OK);
		}
	}

	public ResponseEntity<String> deleteRole(int id) {
		Optional<Roles> roles = rolesRepository.findById(id);
		if (roles.isPresent()) {
			rolesRepository.deleteById(id);
			return new ResponseEntity<>("Role Deleted ", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Role with id " + id + " is not found", HttpStatus.OK);
		}
	}

}

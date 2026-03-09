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

    public ResponseEntity<String> addRole(RolesDTO roleDto) {

  
        if (roleDto.getRole() == null || roleDto.getRole().isBlank()) {
            return new ResponseEntity<>("Role name cannot be empty", HttpStatus.OK);
        }

        String roleName = roleDto.getRole().trim().toUpperCase();

        // Length validation
        if (roleName.length() < 2 || roleName.length() > 20) {
            return new ResponseEntity<>(
                "Role name must be between 2 and 20 characters",
                HttpStatus.OK
            );
        }

        Optional<Roles> existingRole = rolesRepository.findByRole(roleName);
        
        if (existingRole.isPresent() && existingRole.get().isStatus()) {
            return new ResponseEntity<>("Role already exists", HttpStatus.OK);
        }

        if (existingRole.isPresent()) {
            Roles role = existingRole.get();
            role.setStatus(true);
            rolesRepository.save(role);
            return new ResponseEntity<>("Role reactivated successfully", HttpStatus.OK);
        }

     
        Roles newRole = new Roles();
        newRole.setRole(roleName);
        newRole.setStatus(true);
        rolesRepository.save(newRole);

        return new ResponseEntity<>("Role added successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteRole(int id) {

        Optional<Roles> roleOptional = rolesRepository.findById(id);

        if (roleOptional.isEmpty()) {
            return new ResponseEntity<>("Role not found", HttpStatus.OK);
        }

        Roles role = roleOptional.get();

        if (!role.isStatus()) {
            return new ResponseEntity<>("Role already deleted", HttpStatus.OK);
        }

        role.setStatus(false);
        rolesRepository.save(role);

        return new ResponseEntity<>("Role deleted successfully", HttpStatus.OK);
    }
}


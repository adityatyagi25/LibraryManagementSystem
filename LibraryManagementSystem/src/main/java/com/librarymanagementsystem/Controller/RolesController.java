package com.librarymanagementsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.RolesDTO;
import com.librarymanagementsystem.Service.RolesService;
@RestController
@RequestMapping("/admin")
public class RolesController {
    @Autowired
	private RolesService rolesService;
    @PostMapping("/addRole")
    public ResponseEntity<String> addRole(@RequestBody RolesDTO role) {
    	return rolesService.addRole(role);
    }
    @DeleteMapping("/deleteRole/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable int id){
    	return rolesService.deleteRole(id);
    }
    
}

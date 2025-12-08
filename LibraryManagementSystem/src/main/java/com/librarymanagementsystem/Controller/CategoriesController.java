package com.librarymanagementsystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagementsystem.DTO.CategoriesDTO;
import com.librarymanagementsystem.DTO.CategoriesDTO2;
import com.librarymanagementsystem.Service.CategoriesService;

@RestController
@RequestMapping("/librarian")
public class CategoriesController {
	@Autowired
	private CategoriesService categoriesService;
	@PostMapping("/addCategory")
	public ResponseEntity<String> addCategory(@RequestBody CategoriesDTO category) {
		return categoriesService.addCategory(category);
	}

	@DeleteMapping("/deleteCategory/{id}")
	public ResponseEntity<String>  deleteCategory(@PathVariable int id) {
		return categoriesService.deleteCategory(id);
	}
	@GetMapping("/getAllCategories")
	public List<CategoriesDTO2> getAllCategories() {
		return categoriesService.getAllCategories();
	}
}

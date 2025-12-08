package com.librarymanagementsystem.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.DTO.CategoriesDTO;
import com.librarymanagementsystem.DTO.CategoriesDTO2;
import com.librarymanagementsystem.Entity.Categories;
import com.librarymanagementsystem.Exception.CategoryNotFoundException;
import com.librarymanagementsystem.Repository.CategoriesRepository;
@Service
public class CategoriesService  {
	@Autowired
	private CategoriesRepository categoriesRepository;

	public ResponseEntity<String> addCategory(CategoriesDTO categoryDto) {
	 Optional<Categories> checking=categoriesRepository.findByCategoryName(categoryDto.getCategoryName());
	 if(checking.isPresent()) {
		 return new ResponseEntity<>("Category Already Present",HttpStatus.OK);
	 }
		Categories category=new Categories();
		category.setCategoryName(categoryDto.getCategoryName());
		categoriesRepository.save(category);
		return new ResponseEntity<>("Category Added",HttpStatus.OK);
		
	}

	public ResponseEntity<String> deleteCategory(int id) {
		Optional<Categories> category=categoriesRepository.findById(id);
		if(category.isPresent()) {
		categoriesRepository.deleteById(id);
		return new ResponseEntity<>("Category Deleted",HttpStatus.OK);
		}
		else {
		return new ResponseEntity<>("Category with ID " + id + " not found",HttpStatus.OK);
		}
	}

	public List<CategoriesDTO2> getAllCategories() {
		   List<Categories> list = categoriesRepository.findAll();
	       List<CategoriesDTO2> dtoList = new ArrayList<>();
 
		for (Categories category : list) {
		    CategoriesDTO2 dto = new CategoriesDTO2();
		    dto.setId(category.getId());
		    dto.setCategoryName(category.getCategoryName());
		    dtoList.add(dto);
		}
		  return dtoList;
	}
	

}

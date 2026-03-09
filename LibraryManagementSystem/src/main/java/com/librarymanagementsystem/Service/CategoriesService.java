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
import com.librarymanagementsystem.Repository.CategoriesRepository;

@Service
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    public ResponseEntity<String> addCategory(CategoriesDTO categoryDto) {

   
        if (categoryDto.getCategoryName() == null || categoryDto.getCategoryName().isBlank()) {
            return new ResponseEntity<>("Category name cannot be empty", HttpStatus.OK);
        }

        String categoryName = categoryDto.getCategoryName().trim().toUpperCase();

        if (categoryName.length() < 2 || categoryName.length() > 20) {
            return new ResponseEntity<>(
                "Please use characters between 2 and 20",
                HttpStatus.OK
            );
        }

        Optional<Categories> existingCategory =
                categoriesRepository.findByCategoryName(categoryName);

     
        if (existingCategory.isPresent() && existingCategory.get().isStatus()) {
            return new ResponseEntity<>("Category already present", HttpStatus.OK);
        }


        if (existingCategory.isPresent()) {
            Categories category = existingCategory.get();
            category.setStatus(true);
            categoriesRepository.save(category);
            return new ResponseEntity<>("Category reactivated", HttpStatus.OK);
        }

        // New category
        Categories category = new Categories();
        category.setCategoryName(categoryName);
        category.setStatus(true);
        categoriesRepository.save(category);

        return new ResponseEntity<>("Category added successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteCategory(int id) {

        Optional<Categories> categoryOptional = categoriesRepository.findById(id);

        if (categoryOptional.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.OK);
        }

        Categories category = categoryOptional.get();

        if (!category.isStatus()) {
            return new ResponseEntity<>("Category already deleted", HttpStatus.OK);
        }

        category.setStatus(false);
        categoriesRepository.save(category);

        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

    public List<CategoriesDTO2> getAllCategories() {

        List<Categories> categories = categoriesRepository.findByStatusTrue();
        List<CategoriesDTO2> dtoList = new ArrayList<>();

        for (Categories category : categories) {
            CategoriesDTO2 dto = new CategoriesDTO2();
            dto.setId(category.getId());
            dto.setCategoryName(category.getCategoryName());
            dtoList.add(dto);
        }
        return dtoList;
    }
}

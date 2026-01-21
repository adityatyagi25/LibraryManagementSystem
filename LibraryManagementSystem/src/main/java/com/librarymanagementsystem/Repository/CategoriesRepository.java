package com.librarymanagementsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagementsystem.Entity.Categories;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
	Optional<Categories> findByCategoryName(String string);

}

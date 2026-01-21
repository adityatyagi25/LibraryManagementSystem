package com.librarymanagementsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagementsystem.Entity.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
	public Optional<Roles> findByRole(String role);

}

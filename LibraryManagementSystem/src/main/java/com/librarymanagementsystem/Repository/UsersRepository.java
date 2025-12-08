package com.librarymanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagementsystem.Entity.Users;
@Repository
public interface UsersRepository extends JpaRepository<Users,String> {


}

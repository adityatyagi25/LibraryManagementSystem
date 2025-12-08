package com.librarymanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagementsystem.Entity.BorrowRecords;
@Repository
public interface BorrowRecordsRepository extends JpaRepository<BorrowRecords,Long>{

}

package com.librarymanagementsystem.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.librarymanagementsystem.Entity.BorrowRecords;

@Repository
public interface BorrowRecordsRepository extends JpaRepository<BorrowRecords, Long> {
	List<BorrowRecords> findByBorrowDateBetween(
            LocalDate startDate,
            LocalDate endDate);
	@Query(value = "SELECT SUM(fine_collected) FROM borrow_records", nativeQuery = true)
	Long getTotalFineCollected();
	@Query(value = "SELECT SUM(fine_amount) FROM borrow_records", nativeQuery = true)
	Long pendingFine();
	
	
	 @Query(
		        value = "SELECT * FROM borrow_records WHERE user_id = :userId",
		        nativeQuery = true
		    )
	List<BorrowRecords> booksIssuedBy(@Param("userId") String id);
	 
	 @Query(
		        value = "SELECT SUM(fine_amount) FROM borrow_records WHERE user_id = :userId",
		        nativeQuery = true
		    )
	Long finePerUser(@Param("userId") String id);
	
	
}

package com.librarymanagementsystem.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.librarymanagementsystem.DTO.CategoriesDTO;
import com.librarymanagementsystem.DTO.MostLeastBorrowedBooksDTO;
import com.librarymanagementsystem.DTO.UserCategoryDTO;
import com.librarymanagementsystem.Entity.BorrowRecords;

@Repository
public interface BorrowRecordsRepository extends JpaRepository<BorrowRecords, Long> {

	List<BorrowRecords> findByBorrowDateBetween(LocalDate startDate, LocalDate endDate);

	@Query(value = "SELECT SUM(fine_collected) FROM borrow_records", nativeQuery = true)
	Long getTotalFineCollected();

	@Query(value = "SELECT SUM(fine_amount) FROM borrow_records", nativeQuery = true)
	Long pendingFine();

	@Query(value = "SELECT * FROM borrow_records WHERE user_id = :userId", nativeQuery = true)
	List<BorrowRecords> booksIssuedBy(@Param("userId") String id);

	@Query(value = "SELECT SUM(fine_amount) FROM borrow_records WHERE user_id = :userId", nativeQuery = true)
	Long finePerUser(@Param("userId") String id);

	Page<BorrowRecords> findByUserEmail(String email, Pageable pageable);

	Page<BorrowRecords> findByBookBookId(long bookId, Pageable pageable);

	@Query(value = "select u.email as email ,b.category_id as category from users u join borrow_records br on u.email=br.user_id join books b on br.book_id=b.book_id where br.user_id= :email", nativeQuery = true)

	List<UserCategoryDTO> userCategoryDTO(String email);

	@Query(value = "SELECT c.category_name " + "FROM books b " + "JOIN borrow_records br ON b.book_id = br.book_id "
			+ "JOIN users u ON br.user_id = u.email " + "JOIN categories c ON b.category_id = c.id "
			+ "WHERE u.email = :email " + "GROUP BY c.category_name " + "ORDER BY COUNT(*) DESC "
			+ "LIMIT 1", nativeQuery = true)
	String favouriteCategory(@Param("email") String email);

	@Query(value = "SELECT b.title, COUNT(*) AS total_borrows\n" + "FROM books b\n"
			+ "JOIN borrow_records br ON b.book_id = br.book_id\n" + "GROUP BY b.title\n"
			+ "ORDER BY total_borrows DESC\n" + "LIMIT 1;", nativeQuery = true)
	MostLeastBorrowedBooksDTO mostBorrowedBook();
	
	@Query(value = "SELECT b.title, COUNT(*) AS total_borrows\n" + "FROM books b\n"
			+ "JOIN borrow_records br ON b.book_id = br.book_id\n" + "GROUP BY b.title\n"
			+ "ORDER BY total_borrows ASC\n" + "LIMIT 1;", nativeQuery = true)
	MostLeastBorrowedBooksDTO leastBorrowedBook();

}

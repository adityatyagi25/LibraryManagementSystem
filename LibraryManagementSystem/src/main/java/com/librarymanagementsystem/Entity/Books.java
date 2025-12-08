package com.librarymanagementsystem.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books", uniqueConstraints = {
	    @UniqueConstraint(columnNames = "isbn")
	})
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank

    private String isbn;

    @NotNull
    private int totalCopies;

    private int availableCopies;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;
    @JsonBackReference
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowRecords> borrowRecords;
}

package com.example.library.entity;

import com.example.library.constants.Category;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Books")
public class Book {


    /**
     * Unique Book Number given by company.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * title of the book
     */
    private String title;

    /**
     * author of the book
     */
    private String author;

    /**
     * publisher of the book
     */
    private String publisher;

    /**
     * category of the book
     * Eg: New, Classic, Standard
     */
    @Enumerated(EnumType.STRING)
    private Category category;

    /**
     * Amount of book available
     */
    @Min(value = 0, message = "All books are rented.")
    private Integer totalCount;

    /**
     * Total copies of book rented
     */
    @Min(value = 0, message = "Total rent should be positive value.")
    private Integer rented;

}

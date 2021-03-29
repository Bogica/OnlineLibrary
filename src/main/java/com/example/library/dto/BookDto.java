package com.example.library.dto;

import com.example.library.constants.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookDto {

    /**
     * unique book Number
     */
    @ApiModelProperty(value="Book Unique Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * title of the book
     */
    @ApiModelProperty(value="Title of the book")
    private String title;

    /**
     * author of the book
     */
    @ApiModelProperty(value="Author of the book")
    private String author;

    /**
     * publisher of the book
     */
    @ApiModelProperty(value="Publisher of the book")
    private String publisher;

    /**
     * category of the book
     * Eg: New, Classic, Standard
     */
    @ApiModelProperty(value="Category of the book")
    private Category category;


    /**
     * amount of book available
     */
    @ApiModelProperty(value="Copies of book available on the library")
    @Min(value = 0, message = "Total totalCount should be positive value.")
    private int totalCount;

    /**
     * total count of book rented
     */
    @ApiModelProperty(value="Copies of book available on the library")
    @Min(value = 0, message = "Total rent should be positive value.")
    private Integer rented;

}

package com.example.library.dto;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    /**
     * unique user number
     */
    private Long id;

    /**
     * first name of user
     */
    private String firstName;

    /**
     * last name of user
     */
    private String lastName;

    /**
     * books rented by user
     */
    private Integer borrowedBooksCount;

}

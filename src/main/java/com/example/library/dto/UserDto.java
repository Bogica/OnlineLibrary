package com.example.library.dto;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer borrowedBooksCount;

}

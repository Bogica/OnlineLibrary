package com.example.library.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Users")
public class User {


    /**
     * unique user number, auto generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

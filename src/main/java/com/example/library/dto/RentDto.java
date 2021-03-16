package com.example.library.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class RentDto {

    /**
     * unique rent Number
     */
    private Long rentId;

    /**
     * unique user Number
     */
    private Long userId;

    /**
     * unique book Number
     */
    private Long bookId;

    /**
     * first name of user
     */
    private String userFirstName;

    /**
     * last name of user
     */
    private String userLastName;

    /**
     * title of the book
     */
    private String bookTitle;

    /**
     * date and time when rent started
     */
    private LocalDateTime rentStart;

    /**
     * object book, rented book
     */
    private LocalDateTime RentEnd;

}
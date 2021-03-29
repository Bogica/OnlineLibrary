package com.example.library.dto;

import lombok.*;
import java.time.LocalDate;


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
     * date when rent started
     */
    private LocalDate rentStart;

    /**
     * date when rent ended
     */
    private LocalDate RentEnd;

    /**
     * if date is overdue
     */
    private boolean isOverdue;

}
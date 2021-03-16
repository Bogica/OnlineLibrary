package com.example.library.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Rent")
public class Rent {

    /**
     * unique rent number, auto generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * date and time when rent started
     */
    private LocalDateTime rentStart;

    /**
     * date and time when rent ended
     */
    private LocalDateTime rentEnd;


    /**
     * object book, rented book
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    /**
     * object user, user who rents book
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}

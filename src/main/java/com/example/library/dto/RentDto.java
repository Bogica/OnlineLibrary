package com.example.library.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class RentDto {

    private Long rentId;

    private Long userId;

    private Long bookId;

    private String userFirstName;

    private String userLastName;

    private String bookTitle;

    private LocalDateTime rentStart;

    private LocalDateTime RentEnd;

}
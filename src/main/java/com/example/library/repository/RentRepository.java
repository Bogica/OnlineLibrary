package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.Rent;
import com.example.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findByUser(User user);

    List<Rent> findByBook(Book book);
}

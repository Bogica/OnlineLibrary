package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

}


package com.example.library.repository;

import com.example.library.constants.Category;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    /*
    List<Book> findAllBookByCategoryName(String categoryName);

     */
}


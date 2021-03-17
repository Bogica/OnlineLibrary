package com.example.library.repository;

import com.example.library.entity.Rent;
import com.example.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findByUser(User user);
}

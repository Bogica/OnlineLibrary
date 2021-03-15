package com.example.library.controller;

import com.example.library.dto.UserDto;
import com.example.library.entity.User;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("/users")
    public List<User> allUsers(){
        return service.allUsers();
    }

    @GetMapping("/userById/{id}")
    public User getUserById(@PathVariable Integer id){
        return service.findUserById(id);
    }


    @PostMapping("/users")
    public User addUser(@RequestBody User user){
        return service.saveUser(user);
    }

    @PostMapping("/addMultipleUsers")
    public List<User> addAllUsers(@RequestBody List<User> users){
        return service.saveAllUsers(users);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody UserDto user){
        return service.updateUser(user);
    }

    @DeleteMapping("/deleteUsers/{id}")
    public String delete(@PathVariable Integer id){
        return service.deleteUser(id);
    }
}

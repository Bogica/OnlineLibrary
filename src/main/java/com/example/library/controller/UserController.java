package com.example.library.controller;

import com.example.library.dto.UserDto;
import com.example.library.entity.User;
import com.example.library.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller for the online library project
 * Acceptance criterias:
 * 1)get all users
 * 2)get user by Id
 * 3)add user
 * 4)add multiple users
 * 5)update user
 * 6)delete user
 */
@RestController
@Api(tags = {"User controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "User controller", description = "User REST Endpoints.")
})
public class UserController {

    @Autowired
    UserService service;

    /**
     *  1)get all users
     * This add a new rent
     *
     */
    @GetMapping("/users")
    public List<UserDto> allUsers(){
        return service.allUsers();
    }

    /**
     *  2)get user by Id
     *
     * @param id
     */
    @GetMapping("/userById/{id}")
    public UserDto getUserById(@PathVariable Integer id){
        return service.findUserById(id);
    }


    /**
     *  3)add user
     */
    @PostMapping("/users")
    public void addUser(@RequestBody UserDto user){
        service.saveUser(user);
    }

    /**
     *  4)add multiple users
     */
    @PostMapping("/addMultipleUsers")
    public void addAllUsers(@RequestBody List<UserDto> users){
        service.saveAllUsers(users);
    }

    /**
     *  5)update user
     */
    @PutMapping("/users")
    public User updateUser(@RequestBody UserDto user){
        return service.updateUser(user);
    }

    /**
     *  6)delete user
     */
    @DeleteMapping("/deleteUsers/{id}")
    public String delete(@PathVariable Integer id){
        return service.deleteUser(id);
    }
}

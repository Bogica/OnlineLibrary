package com.example.library.service;

import com.example.library.dto.UserDto;
import com.example.library.entity.User;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> allUsers(){
        return userRepository.findAll();
    }

    public User findUserById(long id){
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> saveAllUsers(List<User> userList){
        return userRepository.saveAll(userList);
    }

    public String deleteUser(long id){
        userRepository.deleteById(id);
        return "User is deleted!";
    }

    public User updateUser(UserDto user){
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());


        return userRepository.save(existingUser);
    }


}

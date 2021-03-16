package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.dto.UserDto;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;


    /**
     * List all the users
     *
     * @return List<UserDto>
     */
    public List<UserDto> allUsers(){
        List<User> allUsers= userRepository.findAll();
        return mapUsersListToUsersDtoList(allUsers);
    }

    /**
     * Get user by id
     *
     * @param id
     * @return userDto
     */
    public UserDto findUserById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id:" + id + " is not found."));

        LOGGER.info("User with id: " + id + " exists.");

        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Add new user into database
     * Save the book In Book Entity
     * @param userDto
     */
    public void saveUser(UserDto userDto){
        Optional<User> userId = userRepository.findById(userDto.getId());
        if(userId.isPresent()){
            throw new DuplicateResourceException("User with this ID already exists");
        }

        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
    }

    /**
     * Add multiple new users into database
     *
     * @param userDto
     */
    public void saveAllUsers(List<UserDto> userDto){
        List<User> userList = Arrays.asList(modelMapper.map(userDto, User.class));
        userRepository.saveAll(userList);
    }

    /**
     * Delete user
     *
     * @param id
     */
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

    //Convert List of users to List of userDto
    private List<UserDto> mapUsersListToUsersDtoList(List<User> users) {
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }


}

package com.example.library.controller;

import com.example.library.dto.BookDto;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.dto.RentDto;
import com.example.library.entity.Book;
import com.example.library.entity.Rent;
import com.example.library.entity.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.RentRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.RentService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@RestController
public class RentController {

    @Autowired
    RentService rentService;;

    /**
     * AC:  1)add a rent
     * This add a new rent
     *
     * @param rentDto
     */
    @ApiOperation(value = "Add New Rent")
    @PostMapping("/addNewRent")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewRent(@RequestBody RentDto rentDto){
        rentService.addNewRent(rentDto);
    }

    /**
     * AC:  2)add multiple rents
     * This add a new rent
     *
     * @param rentDto, userId
     */

    @ApiOperation(value = "Add Multiple Rents")
    @PostMapping("/addMultipleRent/{userId}")
    @ResponseStatus(HttpStatus.CREATED)

    public void addMultipleRent(@RequestBody List<RentDto> rentDto, @PathVariable Long userId){
        rentService.addMultipleRents(rentDto, userId);
    }


    /**
     * AC: 4)get all rents
     *
     * @return List<RentDto>
     */
    @ApiOperation(value = "Get All Rents")
    @GetMapping("/displayAll")
    public List<RentDto> displayAllRents(){
        return rentService.getAllRents();
    }

    /**
     * AC: 5)return book
     *
     * @param rentId
     */
    @ApiOperation(value = "Return Book")
    @PutMapping("/rentEnd/{rentId}")
    public void returnBook(@PathVariable Long rentId, @RequestBody RentDto rentDto) throws BadRequestException {
        rentService.returnBook(rentId, rentDto);
    }

    /**
     * AC: 5)find by userId
     *
     * @param userId
     */
    @ApiOperation(value = "Find Rent By UserId")
    @GetMapping("/rentByUserId/{userId}")
    public List<RentDto> findRentByUserId(@PathVariable Long userId){
        return rentService.findByUserId(userId);
    }

    /**
     * AC: 5)Rent alert
     *
     * @param userId
     */
    @ApiOperation(value = "Did rent passed 30 days")
    @GetMapping("/rentAlert/{userId}")
    public List<Rent> rentAlert(@PathVariable Long userId){
        return rentService.rentAlert(userId);
    }


    /**
     * AC: 6)update rent
     */
    /*
    @ApiOperation(value = "Update rent")
    @PutMapping("/updateRent")
    @ResponseStatus(HttpStatus.OK)
    public void updateRent(@Valid @RequestBody RentDto rentDto){
        rentService.updateRent(rentDto);
    }

    /**
     * AC: 7)delete rent
     * @param id
     */
    /*
    @ApiOperation(value = "Delete Rent By Id")
    @DeleteMapping("/deleteRentById/{id}")
    public void deleteRent(@PathVariable Long id){
        rentService.deleteRent(id);
    }

     */

}

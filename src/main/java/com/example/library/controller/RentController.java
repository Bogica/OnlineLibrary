package com.example.library.controller;

import com.example.library.constants.Category;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller for the online library project
 * Acceptance criterias:
 * 1)add new rent
 * 2)add multiple rents
 * 3)get all rents
 * 4)return book (rent ended)
 * 5)find rent by userId
 * 6)Rent alert
 */
@RestController
@Api(tags = {"Rent controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Rent controller", description = "Rent REST Endpoints.")
})
public class RentController {

    @Autowired
    RentService rentService;;

    /**
     * 1)add new rent
     * This add a new rent
     *
     * @param rentDto, UserId
     */
    @ApiOperation(value = "Add New Rent")
    @PostMapping("/addNewRent/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewRent(@RequestBody RentDto rentDto, @PathVariable Long userId){
        rentService.addNewRent(rentDto, userId);
    }

    /**
     * 2)add multiple rents
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
     * 3)get all rents
     *
     * @return List<RentDto>
     */
    @ApiOperation(value = "Get All Rents")
    @GetMapping("/displayAll")
    public List<RentDto> displayAllRents(){
        return rentService.getAllRents();
    }

    /**
     * 4)return book (rent ended)
     *
     * @param rentId
     */
    @ApiOperation(value = "Return Book")
    @PutMapping("/rentEnd/{rentId}")
    public void returnBook(@PathVariable Long rentId, @RequestBody RentDto rentDto) throws BadRequestException {
        rentService.returnBook(rentId, rentDto);
    }

    /**
     * 5)find rent by userId
     *
     * @param userId
     */
    @ApiOperation(value = "Find Rent By UserId")
    @GetMapping("/rentByUserId/{userId}")
    public List<RentDto> findRentByUserId(@PathVariable Long userId){
        return rentService.findByUserId(userId);
    }


    /**
     * 7)update rent
     */
    /*
    @ApiOperation(value = "Update rent")
    @PutMapping("/updateRent")
    @ResponseStatus(HttpStatus.OK)
    public void updateRent(@Valid @RequestBody RentDto rentDto){
        rentService.updateRent(rentDto);
    }

    /**
     * 8)delete rent
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

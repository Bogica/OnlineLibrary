package com.example.library.controller;

import com.example.library.dto.RentDto;
import com.example.library.service.RentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for the online library project
 * Acceptance criterias:
 * 1)add new rent
 * 2)get all rents
 * 3)return book (rent ended)
 * 4)find rent by userId
 * 5)update rent
 * 6)delete rent
 */
@RestController
@Api(tags = {"Rent controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Rent controller", description = "Rent REST Endpoints.")
})
public class RentController {

    @Autowired
    RentService rentService;
    ;

    /**
     * 1)add new rent
     * This add a new rent
     *
     * @param rentDto, UserId
     */
    @ApiOperation(value = "Add Multiple Rents")
    @PostMapping("/addNewRents/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewRents(@RequestBody List<RentDto> rentDto, @PathVariable Long userId) {
        rentService.addNewRents(rentDto, userId);
    }

    /**
     * 2)get all rents
     *
     * @return List<RentDto>
     */
    @ApiOperation(value = "Get All Rents")
    @GetMapping("/displayAll")
    public List<RentDto> displayAllRents() {
        return rentService.getAllRents();
    }

    /**
     * 3)return book (rent ended)
     *
     * @param rentId
     */
    @ApiOperation(value = "Return Book")
    @PutMapping("/rentEnd/{rentId}")
    public void returnBook(@PathVariable Long rentId, @RequestBody RentDto rentDto) {
        rentService.returnBook(rentId, rentDto);
    }

    /**
     * 4)find rent by userId
     *
     * @param userId
     */
    @ApiOperation(value = "Find Rent By UserId")
    @GetMapping("/rentByUserId/{userId}")
    public List<RentDto> findRentByUserId(@PathVariable Long userId) {
        return rentService.findByUserId(userId);
    }


    /**
     * 5)update rent
     */
    @ApiOperation(value = "Update rent")
    @PutMapping("/updateRent")
    @ResponseStatus(HttpStatus.OK)
    public void updateRent(@RequestBody RentDto rentDto) {
        rentService.updateRent(rentDto);
    }

    /**
     * 6)delete rent
     *
     * @param id
     */
    @ApiOperation(value = "Delete Rent By Id")
    @DeleteMapping("/deleteRentById/{id}")
    public void deleteRent(@PathVariable Long id) {
        rentService.deleteRent(id);
    }

}

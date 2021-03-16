package com.example.library.service;

import com.example.library.constants.Category;
import com.example.library.controller.RentController;
import com.example.library.dto.BookDto;
import com.example.library.dto.RentDto;
import com.example.library.entity.Book;
import com.example.library.entity.Rent;
import com.example.library.entity.User;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.RentRepository;
import com.example.library.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentController.class);

    final ModelMapper modelMapper = new ModelMapper();

    public final int MAX_BOOKS_PER_USER = 7;
    public final int MAX_BOOKS_PER_RENT = 5;
    public final int MAX_BOOKS_OF_NEW_CATEGORY = 2;
    public final int OVERDUE_DAYS = 30;


    @Autowired
    RentRepository rentRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Add new rent to database
     *
     * @param rentDto
     */
    public void addNewRent(RentDto rentDto, long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: " + rentDto.getUserId() + " is not found"));
        Book book = bookRepository.findById(rentDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + rentDto.getBookId() + " is not found."));

        List<Rent> rentList = rentRepository.findByUser(user);

        int categoryCount = 0;

        for(Rent r : rentList){
            if(r.getBook().getCategory() == Category.NEW){
                categoryCount++;
            }
        }

        if(book.getCategory() == Category.NEW){
            categoryCount++;

            if (categoryCount > MAX_BOOKS_OF_NEW_CATEGORY){
                throw new BookNotFoundException("You can't borrow more then 2 NEW category books");
            }
        }

        int borrowedBooksCount = user.getBorrowedBooksCount() + 1;
        LOGGER.info("Setting bookTotalCount less by 1 and setting borrowedBooksCount to increase by 1.");

        if(borrowedBooksCount > MAX_BOOKS_PER_USER){
            throw new BookNotFoundException("You have reached your limit to borrow books. Return a book and then you can borrow again");
        }
        user.setBorrowedBooksCount(borrowedBooksCount);
        userRepository.save(user);

        int bookTotalCount = book.getTotalCount() - 1;
        if (bookTotalCount == 0 ) {
            throw new BookNotFoundException("All books are rented");
        }

        int rented = book.getRented();
        book.setRented(rented + 1);

        book.setTotalCount(bookTotalCount);

        bookRepository.save(book);

        Rent rent = new Rent();
        rent.setUser(user);
        rent.setBook(book);
        rent.setRentStart(LocalDate.now());

        rentRepository.save(rent);
    }
    
    /**
     * Add list of rents to database
     *
     * @param rentDtoList, userId
     */
    public void addMultipleRents(List<RentDto> rentDtoList, long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: " + userId + " is not found"));

        List<Rent> rentList = rentRepository.findByUser(user);

        if(rentDtoList.size() > MAX_BOOKS_PER_RENT){
            throw new BookNotFoundException("You can only rent 5 books at time.");
        }

        int categoryCount = 0;

        for(Rent r : rentList){
            if(r.getBook().getCategory() == Category.NEW){
                categoryCount++;
            }
        }

        for(RentDto dto : rentDtoList) {
            addNewRent(dto, userId);
        }

    }

    /**
     * Get rent by userId
     *
     * Check if date of return  is overdue
     *
     * @param userId
     * @return rentdto
     */

    public List<RentDto> findByUserId(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " is not found"));
        List<Rent> rent = rentRepository.findByUser(user);
        List<RentDto> rentByUser = new ArrayList<>();

        boolean flag = false;
        for(Rent r : rent){

            LocalDate rentDate = r.getRentStart();
            LocalDate dueDate = rentDate.plusDays(30);
            if (dueDate.isBefore(LocalDate.now(ZoneId.of("Europe/Berlin")))){
                flag = true;
                throw new BookNotFoundException("You are late with book return for " + dueDate + " days");
            }

            rentByUser.add(modelMapper.map(r, RentDto.class));
        }
        return rentByUser;
    }

    /**
     * List all the rents
     *
     * @return List<RentDto>
     */
    public List<RentDto> getAllRents(){
        List<Rent> rents = rentRepository.findAll();
        return mapRentListToRentDtoList(rents);
    }

    //Convert List of books to List of bookDto
    private List<RentDto> mapRentListToRentDtoList(List<Rent> rents) {
        return rents.stream()
                .map(rent -> modelMapper.map(rent, RentDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Return book
     *
     * @param id
     */
    public void returnBook(long id, RentDto rentDto) throws BadRequestException {
        Rent borrowed = rentRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("Rent with id: " + id + " is not found"));
        User user = userRepository.findById(rentDto.getUserId())
                .orElseThrow(()-> new UserNotFoundException("User with id: " + rentDto.getUserId() + " is not found"));
        Book book = bookRepository.findById(rentDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + rentDto.getBookId() + " is not found."));


        int bookTotalCount = book.getTotalCount() + 1;
        if (bookTotalCount <= 0) {
            try {
                throw new BadRequestException("TotalCount cannot be negative. Not enough book in store to sell.");
            } catch (BadRequestException e) {
                e.printStackTrace();
            }
        }

        int rented = book.getRented();
        book.setRented(rented - 1);
        book.setTotalCount(bookTotalCount);
        bookRepository.save(book);

        int borrowedBooksCount = user.getBorrowedBooksCount() - 1;
        if(borrowedBooksCount <= 0 ){
            throw new BadRequestException("BorrowedBooksCount cannot be negative.");
        }

        LOGGER.info("Setting total amount less by 1 and setting bookTotalCount to increase by 1.");

        user.setBorrowedBooksCount(borrowedBooksCount);
        userRepository.save(user);

        Rent rent = borrowed;
        rent.setBook(book);
        rent.setRentEnd(LocalDate.now());


        rentRepository.save(rent);
    }






}

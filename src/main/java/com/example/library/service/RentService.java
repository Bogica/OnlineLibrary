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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentController.class);

    final ModelMapper modelMapper = new ModelMapper();

    public final int MAX_BOOKS_PER_USER = 8;
    public final int RETURN_DEADLINE_DAYS = 30;

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
    @Transactional
    public void addNewRent(@RequestBody RentDto rentDto){
        User user = userRepository.findById(rentDto.getUserId())
                .orElseThrow(()-> new UserNotFoundException("User with id: " + rentDto.getUserId() + " is not found"));
        Book book = bookRepository.findById(rentDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + rentDto.getBookId() + " is not found."));

        int bookTotalCount = book.getTotalCount() - 1;
        if (bookTotalCount == 0 ) {
            throw new BookNotFoundException("All books are rented");
        }

        int rented = book.getRented();
        book.setRented(rented + 1);

        book.setTotalCount(bookTotalCount);

        bookRepository.save(book);

        int categoryCount = 0;

        if(book.getCategory() == Category.NEW){
            categoryCount++;
        }else if (categoryCount == 2){
            throw new BookNotFoundException("You can't borrow more then 2 NEW category books");
        }

        int borrowedBooksCount = user.getBorrowedBooksCount() + 1;
        LOGGER.info("Setting bookTotalCount less by 1 and setting borrowedBooksCount to increase by 1.");

        if(borrowedBooksCount >= MAX_BOOKS_PER_USER){
            throw new BookNotFoundException("You have reached your limit to borrow books. Return a book and then you can borrow again");
        }
        user.setBorrowedBooksCount(borrowedBooksCount);
        userRepository.save(user);

        Rent rent = new Rent();
        rent.setUser(user);
        rent.setBook(book);
        rent.setRentStart(LocalDateTime.now());

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

        List<Rent> tmpList = new ArrayList<>();

        int rentCounter = 0;
        int categoryCount = 0;
        for(RentDto dto : rentDtoList) {
            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new BookNotFoundException("Book with id: " + dto.getBookId() + " is not found."));

            Rent newRent =  new Rent();

            newRent.setBook(book);
            newRent.setUser(user);
            newRent.setRentStart(LocalDateTime.now());

            tmpList.add(newRent);
            rentCounter++;

            if(rentCounter == 5){
                throw new BookNotFoundException("You can only rent 5 books at time.");
            }

            if(book.getCategory() == Category.NEW){
                categoryCount++;
            }else if (categoryCount == 2){
                throw new BookNotFoundException("You can't borrow more then 2 NEW category books");
            }


        }

        rentRepository.saveAll(tmpList);

    }

    /**
     * Get the list of books according to category
     *
     * @param category
     * @return
     */
    /*
    public List<BookDto> getBookByCategory(Category category) {
        LOGGER.info("Fetch all the books by category");
        List<Book> book = bookRepository.findAllBookByCategory(category);
        return mapBookListToBooDtoList(book);
    }



    //Convert List of books to List of bookDto
    private List<BookDto> mapBookListToBooDtoList(List<Book> books) {
        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public List<BookDto> getBookByCategory(String categoryName) {
        LOGGER.info("Fetch all the books by category");
        List<Book> book = bookRepository.findAllBookByCategoryName(categoryName);
        return mapBookListToBooDtoList(book);
    }
*/




    /**
     * Get rent by userId
     *
     * @param userId
     * @return rentdto
     */
    public List<RentDto> findByUserId(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " is not found"));
        List<Rent> rent = rentRepository.findByUser(user);
        List<RentDto> rentByUser = new ArrayList<>();

        for(Rent r : rent){
            rentByUser.add(modelMapper.map(r, RentDto.class));
        }
        return rentByUser;
    }

    /**
     * Get rent by bookId
     *
     * @param bookId
     * @return rentdto
     */
    public List<RentDto> findByBookId(long bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + bookId + " is not found"));
        List<Rent> rent = rentRepository.findByBook(book);
        List<RentDto> rentByBook = new ArrayList<>();

        for(Rent r : rent){
            rentByBook.add(modelMapper.map(r, RentDto.class));
        }
        return rentByBook;
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
        Optional<Rent> borrowed = rentRepository.findById(id);
        if(!borrowed.isPresent()){
            throw new UserNotFoundException("Rent with id: " + id + " is not found");
        }
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

        Rent rent = borrowed.get();
        rent.setBook(book);
        rent.setRentEnd(LocalDateTime.now());


        rentRepository.save(rent);
    }


    /**
     * Book alert
     *
     * @param userId
     */
    public List<Rent> rentAlert(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " is not found"));

        List<Rent> renList = rentRepository.findByUser(user);
        List<Rent> mustReturn = new ArrayList<>();

        for(Rent r : renList){
            LocalDateTime overDueDate = r.getRentStart().plusDays(30); //start + 30 days
            if(r.getRentStart() == null && LocalDateTime.MAX.isAfter(overDueDate)){
                mustReturn.add(r);
            }else
                throw new BookNotFoundException("There is no date overdue");
        }

        return mustReturn;


    }



}

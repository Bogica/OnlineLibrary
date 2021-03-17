package com.example.library.service;

import com.example.library.constants.Category;
import com.example.library.controller.RentController;
import com.example.library.dto.RentDto;
import com.example.library.entity.Book;
import com.example.library.entity.Rent;
import com.example.library.entity.User;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.LimitedNumberException;
import com.example.library.exception.RentNotFoundException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.RentRepository;
import com.example.library.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
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
     * Add new rent
     *
     * @param rentDtoList, userId
     */
    @Transactional
    public void addNewRents(List<RentDto> rentDtoList, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " is not found"));

        List<Rent> rentList = rentRepository.findByUser(user);
        List<Rent> newRent = new ArrayList<>(rentDtoList.size());

        if (rentDtoList.size() > MAX_BOOKS_PER_RENT) {
            throw new LimitedNumberException("You can only rent 5 books at time.");
        }

        int categoryCount = 0;

        for (Rent r : rentList) {
            if (r.getBook().getCategory() == Category.NEW) {
                categoryCount++;
            }
        }

        for (RentDto dto : rentDtoList) {
            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new BookNotFoundException("Book with id: " + dto.getBookId() + " is not found."));

            if (book.getCategory() == Category.NEW) {
                categoryCount++;

                if (categoryCount > MAX_BOOKS_OF_NEW_CATEGORY) {
                    throw new LimitedNumberException("You can't borrow more then 2 NEW category books");
                }
            }

            int borrowedBooksCount = user.getBorrowedBooksCount() + 1;
            LOGGER.info("Setting bookTotalCount less by 1 and setting borrowedBooksCount to increase by 1.");

            if (borrowedBooksCount > MAX_BOOKS_PER_USER) {
                throw new LimitedNumberException("You have reached your limit to borrow books. Return a book and then you can borrow again");
            }
            user.setBorrowedBooksCount(borrowedBooksCount);
            userRepository.save(user);

            int bookTotalCount = book.getTotalCount() - 1;
            if (bookTotalCount == 0) {
                throw new LimitedNumberException("All books are rented");
            }

            int rented = book.getRented();
            book.setRented(rented + 1);

            book.setTotalCount(bookTotalCount);

            bookRepository.save(book);

            Rent rent = new Rent();
            rent.setUser(user);
            rent.setBook(book);
            rent.setRentStart(LocalDate.now());

            newRent.add(rent);

            rentRepository.saveAll(newRent);
        }

    }

    /**
     * Get rent by userId
     * <p>
     * Check if date of return is overdue
     *
     * @param userId
     * @return rentdto
     */
    public List<RentDto> findByUserId(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " is not found"));
        List<Rent> rent = rentRepository.findByUser(user);
        List<RentDto> rentByUser = new ArrayList<>();

        for (Rent r : rent) {
            LocalDate rentDate = r.getRentStart();
            LocalDate dueDate = rentDate.plusDays(OVERDUE_DAYS);
            if (dueDate.isAfter(LocalDate.now(ZoneId.of("Europe/Berlin")))) {
                r.setOverdue(true);
                rentRepository.save(r);
                LOGGER.info("Book is returned late");
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
    public List<RentDto> getAllRents() {
        List<Rent> rents = rentRepository.findAll();
        return mapRentListToRentDtoList(rents);
    }

    /**
     * Return book
     *
     * @param id
     */
    @Transactional
    public void returnBook(long id, RentDto rentDto) {
        Rent borrowed = rentRepository.findById(id)
                .orElseThrow(() -> new RentNotFoundException("Rent with id: " + id + " is not found"));
        User user = userRepository.findById(rentDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with id: " + rentDto.getUserId() + " is not found"));
        Book book = bookRepository.findById(rentDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + rentDto.getBookId() + " is not found."));

        int bookTotalCount = book.getTotalCount() + 1;
        if (bookTotalCount <= 0) {
            throw new LimitedNumberException("TotalCount cannot be negative. Not enough book in store to sell.");
        }

        int rented = book.getRented();
        book.setRented(rented - 1);
        book.setTotalCount(bookTotalCount);
        bookRepository.save(book);

        int borrowedBooksCount = user.getBorrowedBooksCount() - 1;
        if (borrowedBooksCount < 0) {
            throw new LimitedNumberException("BorrowedBooksCount cannot be negative.");
        }

        LOGGER.info("Setting total amount less by 1 and setting bookTotalCount to increase by 1.");

        user.setBorrowedBooksCount(borrowedBooksCount);
        userRepository.save(user);

        Rent rent = borrowed;
        rent.setBook(book);
        rent.setRentEnd(LocalDate.now());
        rentRepository.save(rent);
    }

    /**
     * Update rent
     *
     * @param rentDto
     */
    @Transactional
    public void updateRent(RentDto rentDto) {
        Rent existingRent = rentRepository.findById(rentDto.getRentId())
                .orElseThrow(() -> new RentNotFoundException("Rent with id: " + rentDto.getRentId() + "doesn't exist"));
        Rent existingBook = rentRepository.findById(rentDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + rentDto.getBookId() + "doesn't exist"));
        Rent existingUser = rentRepository.findById(rentDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with id: " + rentDto.getUserId() + "doesn't exist"));

        existingRent.setId(existingRent.getId());
        existingRent.setUser(existingUser.getUser());
        existingRent.setBook(existingBook.getBook());
        Rent rent = modelMapper.map(existingRent, Rent.class);
        rentRepository.save(rent);
    }

    /**
     * Delete rent
     *
     * @param id
     */
    public void deleteRent(long id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Rent with id: " + id + "doesn't exist"));
        rentRepository.delete(rent);
    }

    //Convert List of books to List of bookDto
    private List<RentDto> mapRentListToRentDtoList(List<Rent> rents) {
        return rents.stream()
                .map(rent -> modelMapper.map(rent, RentDto.class))
                .collect(Collectors.toList());
    }

}

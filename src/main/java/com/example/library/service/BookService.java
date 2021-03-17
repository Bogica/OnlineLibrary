package com.example.library.service;

import com.example.library.constants.Category;
import com.example.library.dto.BookDto;
import com.example.library.entity.Book;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    BookRepository bookRepository;

    /**
     * Add new book into database
     *
     * @param bookDto
     */
    @Transactional
    public void addNewBook(BookDto bookDto){
        Optional<Book> bookId = bookRepository.findById(bookDto.getId());
        if(bookId.isPresent()){
            throw new DuplicateResourceException("Book with id: " + bookId + " already exists!");
        }

        LOGGER.info("No duplicate found");
        Book book = modelMapper.map(bookDto, Book.class);

        bookRepository.save(book);
    }

    /**
     * Add multiple new books into database
     *
     * @param bookDto
     */
    @Transactional
    public void addAllBooks(List<BookDto> bookDto){
        List<Book> book = Arrays.asList(modelMapper.map(bookDto, Book.class));
        bookRepository.saveAll(book);
    }

    /**
     * Get book by id
     *
     * @param id
     * @return bookdto
     */
    public BookDto findBookById(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id:" + id + " is not found."));

        LOGGER.info("Book with id: " + id + " exists.");

        return modelMapper.map(book, BookDto.class);
    }

    /**
     * List all the books
     *
     * @return List<BookDto>
     */
    public List<BookDto> allBooks(){
        List<Book> books = bookRepository.findAll();
        return mapBookListToBooDtoList(books);
    }

    /**
     * Update book
     *
     * @param bookDto
     */
    @Transactional
    public void updateBook (BookDto bookDto){
        Book book = modelMapper.map(bookDto, Book.class);

        Book existingBook = bookRepository.findById(book.getId())
                .orElseThrow(() -> new BookNotFoundException("Book doesn't exist."));

        existingBook.setTitle(bookDto.getTitle());
        existingBook.setAuthor(bookDto.getAuthor());
        existingBook.setPublisher(bookDto.getPublisher());
        existingBook.setTotalCount(bookDto.getTotalCount());

        bookRepository.save(existingBook);
    }

    /**
     * Delete book
     *
     * @param id
     */
    public void deleteBook(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id:" + id + " is not found."));

        LOGGER.info("Book with id: " + id + " exists.");

        bookRepository.deleteById(id);
    }

    /**
     * Get the list of books according to category
     *
     * @param category
     * @return
     */
    public List<BookDto> getBookByCategory(Category category) {
        LOGGER.info("Fetch all the books by category");
        List<Book> book = bookRepository.findByCategory(category);
        return mapBookListToBooDtoList(book);
    }

    //Convert List of books to List of bookDto
    private List<BookDto> mapBookListToBooDtoList(List<Book> books) {
        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

}

package com.example.library.service;

import com.example.library.constants.Category;
import com.example.library.dto.BookDto;
import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class BookServiceTest {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;


    @Test
    void addNewBook() {
        BookDto bookDto = Mockito.mock(BookDto.class);
        Book book = Mockito.mock(Book.class);
        bookDto.setId(1L);
        bookDto.setTitle("Harry Potter");
        bookDto.setAuthor("J. K. Rowling");
        bookDto.setPublisher("Bloomsbury Publishing");
        bookDto.setCategory(Category.NEW);
        bookDto.setTotalCount(10);
        bookDto.setRented(0);

        book = modelMapper.map(bookDto, Book.class);

        Mockito.when(bookRepository.save(book)).thenReturn(book);
        assertEquals(book, bookService.addNewBook(bookDto));

    }

    @Test
    void findBookById() {
        /*
        book.setId(2L);
        book.setTitle("Harry Potter");
        book.setAuthor("J. K. Rowling");
        book.setPublisher("Bloomsbury Publishing");
        book.setCategory(Category.CLASSIC);
        book.setTotalCount(10);
        book.setRented(0);

        Mockito.when(bookRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(book));
        bookDto = bookService.findBookById(2L);
        assertNotNull(bookDto);
        assertEquals("Harry Potter", bookDto.getTitle());
        assertEquals(bookDto, bookService.findBookById(2L));

         */
    }


    @Test
    void updateBook() {
    }

    @Test
    void getBookByCategory() {
    }


    @Test
    void allBooks() {
        /*
        book.setId(2L);
        book.setTitle("Harry Potter");
        book.setAuthor("J. K. Rowling");
        book.setPublisher("Bloomsbury Publishing");
        book.setCategory(Category.NEW);
        book.setTotalCount(10);
        book.setRented(0);

        book1.setId(3L);
        book1.setTitle("1984");
        book1.setAuthor("George Orwell");
        book1.setPublisher("George Orwell Publishing");
        book1.setCategory(Category.CLASSIC);
        book1.setTotalCount(10);
        book1.setRented(2);


        books.add(book);
        books.add(book1);

        bookList = Arrays.asList(modelMapper.map(books, BookDto.class));

        Mockito.when(bookRepository.findAll()).thenReturn(books);
        assertEquals(bookList, bookService.allBooks());
*/

    }

    @Test
    void deleteBook() {
    }


}

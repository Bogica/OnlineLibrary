package com.example.library.entity;

import com.example.library.constants.Category;
import com.example.library.dto.BookDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
class BookTest {

    private static final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void checkBookMapping(){

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Harry Potter");
        bookDto.setAuthor("J. K. Rowling");
        bookDto.setPublisher("Bloomsbury Publishing");
        bookDto.setCategory(Category.NEW);
        bookDto.setTotalCount(10);

        Book book = modelMapper.map(bookDto, Book.class);

        assertEquals(bookDto.getId(), book.getId());
        assertEquals(bookDto.getTitle(), book.getTitle());
        assertEquals(bookDto.getAuthor(), book.getAuthor());
        assertEquals(bookDto.getPublisher(), book.getPublisher());
        assertEquals(bookDto.getCategory(), book.getCategory());
        assertEquals(bookDto.getTotalCount(), book.getTotalCount());
    }

}
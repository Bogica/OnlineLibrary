package com.example.library.controller;


import com.example.library.dto.BookDto;
import com.example.library.entity.Book;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the online library project
 * Acceptance criterias:
 * 1)add new book
 * 2)add multiple books
 * 3)get books by id
 * 4)get all books
 * 5)update book
 * 6)delete book
 */
@RestController
@Api(tags = {"Book controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Book controller", description = "Book REST Endpoints.")
})
public class BookController {


    @Autowired
    private BookService bookService;


    /**
     * 1)add new book
     * This add new book with new Identifier.
     *
     * @param bookDto
     */
    @ApiOperation(value = "Add New Book")
    @PostMapping("/addNewBook")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addNewBook(@Valid @RequestBody BookDto bookDto) {
        bookService.addNewBook(bookDto);
    }


    /**
     * 2)add multiple books
     * This add multiple new boopropertyPath=title,ks with new Identifier.
     *
     * @param bookDto
     */
    @ApiOperation(value = "Add Multiple New Books")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addMultipleBooks")
    public void addAllBooks(@RequestBody List<BookDto> bookDto){
        bookService.addAllBooks(bookDto);
    }

    /**
     * 3)get books by id
     *
     * @param id
     * @return bookDto
     */
    @ApiOperation(value = "Get Book By Id")
    @GetMapping("/booksById/{id}")
    public BookDto findBookById(@PathVariable Long id){
        return bookService.findBookById(id);
    }

    /**
     * 4)get all books
     *
     * @return List<bookDto>
     */
    @ApiOperation(value = "Get All Books")
    @GetMapping("/allBooks")
    public List<BookDto> findAllBooks(){
        return bookService.allBooks();
    }


    /**
     * 5)update book
     */
    @ApiOperation(value = "Update Book")
    @PutMapping("/updateBook")
    @ResponseStatus(HttpStatus.OK)
    public void updateBook(@Valid @RequestBody BookDto bookDto){
         bookService.updateBook(bookDto);
    }

    /**
     * 6)delete book
     * @param id
     */
    @ApiOperation(value = "Delete Book By Id")
    @DeleteMapping("/deleteBookById/{id}")
    public void deleteBook(@PathVariable Long id){
         bookService.deleteBook(id);
    }


    /**
     * 7)get books by category
     *
     * @param category EG: NEW, STANDARD, CLASSIC. Check the category enum
     * @return
     */
    /*
    @ApiOperation(value = "Get Book by Category")
    @GetMapping("/booksCategory}")
    public List<BookDto> getBookByCategory(@RequestParam Category category) {
        return rentService.getBookByCategory(category);
    }


    @GetMapping("/bookByCategory/{categoryName}")
    public List<BookDto> bookByCategory(@PathVariable String categoryName) {
        return rentService.getBookByCategory(categoryName);
    }

*/

}

package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import com.epam.rd.autocode.spring.project.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String viewAllBooks(

            Model model){
        List<BookDTO> books =  bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/{name}")
    public String viewBookInfo(@PathVariable String name, Model model) {
        model.addAttribute("book", bookService.getBookByName(name));
        return "book-info";
    }

    @GetMapping("/books/add")
    public String addBookForm(
            Model model){
        model.addAttribute("book", new BookDTO(
                "",
                "",
                AgeGroup.OTHER,
                BigDecimal.ZERO,
                null,
                "",
                0,
                "",
                "",
                Language.OTHER
        ));
        return "book-add";
    }

    @PostMapping("/books/add")
    public String addBook(
            @Valid @ModelAttribute("book") BookDTO book,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book-add";
        }
        bookService.addBook(book);
        return "redirect:/books";
    }

    @PostMapping("/books/{name}/delete")
    public String deleteBook(@PathVariable String name) {
        bookService.deleteBookByName(name);
        return "redirect:/books";
    }

    @GetMapping("/books/{name}/edit")
    public String viewEditForm(
            @PathVariable("name") String name,
            Model model
    ){
        BookDTO book = bookService.getBookByName(name);
        model.addAttribute("book", book);
        return "book-edit";
    }

    @PostMapping("/books/{name}/edit")
    public String editBook(
            @PathVariable("name") String name,
            @Valid @ModelAttribute("book") BookDTO book,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            return "book-edit";
        }
        bookService.updateBookByName(name, book);
        return "redirect:/books";
    }

}

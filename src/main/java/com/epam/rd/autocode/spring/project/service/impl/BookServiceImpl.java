package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.mapper.Impl.BookMapper;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookMapper.mapToDtoList(bookRepository.findAll());
    }

    @Override
    public BookDTO getBookByName(String name) {
        Book book = bookRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Book with name: " + name + " not found"));
        return bookMapper.mapToDto(book);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public BookDTO updateBookByName(String name, BookDTO book) {
        Book currBook = bookRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Book with name: " + name + " not found"));

        currBook.setName(book.getName());
        currBook.setGenre(book.getGenre());
        currBook.setAgeGroup(book.getAgeGroup());
        currBook.setPrice(book.getPrice());
        currBook.setPublicationDate(book.getPublicationDate());
        currBook.setAuthor(book.getAuthor());
        currBook.setPages(book.getPages());
        currBook.setCharacteristics(book.getCharacteristics());
        currBook.setDescription(book.getDescription());
        currBook.setLanguage(book.getLanguage());

        Book savedBook = bookRepository.save(currBook);
        return bookMapper.mapToDto(savedBook);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void deleteBookByName(String name) {
        Book book = bookRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Book with name: " + name + " not found"));
        bookRepository.delete(book);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public BookDTO addBook(BookDTO bookDto) {
        Book book = bookMapper.mapFromDto(bookDto);
        book.setId(null);
        Book savedBook = bookRepository.save(book);
        return bookMapper.mapToDto(savedBook);
    }
}

package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.mapper.Impl.BookMapper;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void addBookMapsAndSavesBookWithoutId() {
        BookDTO bookDTO = BookDTO.builder().name("Book").build();
        Book book = new Book();
        book.setId(1L);
        when(bookMapper.mapFromDto(bookDTO)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookMapper.mapToDto(book)).thenReturn(bookDTO);

        assertSame(bookDTO, bookService.addBook(bookDTO));

        ArgumentCaptor<Book> savedBook = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(savedBook.capture());
        assertNull(savedBook.getValue().getId());
    }

    @Test
    void getBookByNameThrowsWhenBookDoesNotExist() {
        when(bookRepository.findByName("Missing")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.getBookByName("Missing"));
    }
}

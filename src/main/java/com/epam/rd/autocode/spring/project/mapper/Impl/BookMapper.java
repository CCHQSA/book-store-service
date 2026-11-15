package com.epam.rd.autocode.spring.project.mapper.Impl;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.mapper.BaseMapper;
import com.epam.rd.autocode.spring.project.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements BaseMapper<Book, BookDTO> {

    @Override
    public Book mapFromDto(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }



        return new Book(
                null,
                bookDTO.getName(),
                bookDTO.getGenre(),
                bookDTO.getAgeGroup(),
                bookDTO.getPrice(),
                bookDTO.getPublicationDate(),
                bookDTO.getAuthor(),
                bookDTO.getPages(),
                bookDTO.getCharacteristics(),
                bookDTO.getDescription(),
                bookDTO.getLanguage()
        );
    }

    @Override
    public BookDTO mapToDto(Book book) {
        if (book == null) {
            return null;
        }
        return BookDTO.builder()
                .name(book.getName())
                .genre(book.getGenre())
                .ageGroup(book.getAgeGroup())
                .price(book.getPrice())
                .publicationDate(book.getPublicationDate())
                .author(book.getAuthor())
                .pages(book.getPages())
                .characteristics(book.getCharacteristics())
                .description(book.getDescription())
                .language(book.getLanguage())
                .build();
    }
}

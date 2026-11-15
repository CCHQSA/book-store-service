package com.epam.rd.autocode.spring.project.dto;

import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Genre is required.")
    private String genre;

    @NotNull(message = "Age group is required.")
    private AgeGroup ageGroup;

    @NotNull(message = "Price is required.")
    @PositiveOrZero(message = "Price cannot be negative.")
    private BigDecimal price;

    @NotNull(message = "Publication date is required.")
    private LocalDate publicationDate;

    @NotBlank(message = "Author is required.")
    private String author;

    @NotNull(message = "Page count is required.")
    @Positive(message = "Page count must be positive.")
    private Integer pages;

    private String characteristics;

    private String description;

    @NotNull(message = "Language is required.")
    private Language language;
}

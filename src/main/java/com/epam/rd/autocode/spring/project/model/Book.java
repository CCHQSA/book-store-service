package com.epam.rd.autocode.spring.project.model;

import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false)
    private AgeGroup ageGroup;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(name = "publication_year", nullable = false)
    private LocalDate publicationDate;

    @NotBlank
    @Column(nullable = false)
    private String author;

    @NotNull
    @Positive
    @Column(name = "number_of_pages", nullable = false)
    private Integer pages;

    private String characteristics;

    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;
}

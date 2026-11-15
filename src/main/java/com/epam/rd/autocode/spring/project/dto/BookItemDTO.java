package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookItemDTO {

    @NotBlank(message = "Book name is required.")
    private String bookName;

    @NotNull(message = "Quantity is required.")
    @Positive(message = "Quantity must be positive.")
    private Integer quantity;
}

package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @Email(message = "Please enter a valid email address.")
    private String clientEmail;

    @Email(message = "Please enter a valid email address.")
    private String employeeEmail;

    @NotNull(message = "Order date is required.")
    private LocalDateTime orderDate;

    @NotNull(message = "Price is required.")
    @PositiveOrZero(message = "Price cannot be negative.")
    private BigDecimal price;

    @NotEmpty(message = "An order must contain at least one book.")
    private List<@Valid BookItemDTO> bookItems;
}

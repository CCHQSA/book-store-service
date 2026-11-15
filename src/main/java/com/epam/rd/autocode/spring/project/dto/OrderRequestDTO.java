package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {

    @NotEmpty(message = "An order must contain at least one book")
    private List<@Valid BookItemDTO> bookItems;
}

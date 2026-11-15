package com.epam.rd.autocode.spring.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderViewDTO {

    private Long id;
    private OrderDTO order;
}

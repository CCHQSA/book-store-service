package com.epam.rd.autocode.spring.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ClientAccountDTO {

    private String email;
    private String name;
    private BigDecimal balance;
    private boolean blocked;
}

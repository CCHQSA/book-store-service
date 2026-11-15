package com.epam.rd.autocode.spring.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "clients")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Client extends User {

    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance cannot be negative")
    private BigDecimal balance;

    public Client(Long id, String email, String password, String name, BigDecimal balance){
        super(id, email, password, name);
        this.balance = balance;
    }
}

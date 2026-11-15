package com.epam.rd.autocode.spring.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Employee extends User {

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+?[0-9\\s\\-()]{7,20}$",
            message = "Please enter a valid phone number"
    )
    private String phone;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    public Employee(Long id, String email, String password, String name, String phone, LocalDate birthDate){
        super(id, email, password, name);
        this.phone = phone;
        this.birthDate = birthDate;
    }
}

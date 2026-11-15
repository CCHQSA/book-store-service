package com.epam.rd.autocode.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, message = "Password must be at least {min} characters long")
    private String password;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between {min} and {max} characters")
    private String name;

    @PositiveOrZero(message = "Balance cannot be negative")
    private BigDecimal balance;
}

package com.epam.rd.autocode.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, message = "Password must be at least {min} characters long")
    private String password;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between {min} and {max} characters")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+?[0-9\\s\\-()]{7,20}$",
            message = "Please enter a valid phone number"
    )
    private String phone;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
}

package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEditRequest {

    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 50, message = "Name must be between {min} and {max} characters")
    private String name;

    @Pattern(
            regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s])(?=.{8,64}$).+$",
            message = "Use at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    private String password;

    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,20}$", message = "Please enter a valid phone number.")
    private String phone;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
}

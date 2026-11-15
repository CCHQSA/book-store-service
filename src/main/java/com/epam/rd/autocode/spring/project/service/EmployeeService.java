package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeByEmail(String email);

    EmployeeDTO updateEmployeeByEmail(String email, EmployeeDTO employee);

    void deleteEmployeeByEmail(String email);

    EmployeeDTO addEmployee(EmployeeDTO employee);

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Please enter a valid email address") String email);
}

package com.epam.rd.autocode.spring.project.mapper.Impl;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.mapper.BaseMapper;
import com.epam.rd.autocode.spring.project.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper implements BaseMapper<Employee, EmployeeDTO> {

    @Override
    public Employee mapFromDto(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }
        return Employee.builder()
                .email(employeeDTO.getEmail())
                .password(employeeDTO.getPassword())
                .name(employeeDTO.getName())
                .phone(employeeDTO.getPhone())
                .birthDate(employeeDTO.getBirthDate())
                .build();
    }

    @Override
    public EmployeeDTO mapToDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        return EmployeeDTO.builder()
                .email(employee.getEmail())
                .name(employee.getName())
                .phone(employee.getPhone())
                .birthDate(employee.getBirthDate())
                .build();
    }
}

package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.mapper.Impl.EmployeeMapper;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeMapper.mapToDtoList(employeeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Employee not found with email: " + email));
        return employeeMapper.mapToDto(employee);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployeeByEmail(String email, EmployeeDTO employeeDto) {
        Employee currEmployee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Employee not found with email: " + email));

        currEmployee.setName(employeeDto.getName());
        currEmployee.setPhone(employeeDto.getPhone());
        currEmployee.setBirthDate(employeeDto.getBirthDate());

        if (employeeDto.getPassword() != null && !employeeDto.getPassword().isBlank()) {
            currEmployee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        }

        Employee savedEmployee = employeeRepository.save(currEmployee);
        return employeeMapper.mapToDto(savedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Employee not found with email: " + email));
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional
    public EmployeeDTO addEmployee(EmployeeDTO employeeDto) {
        if (existsByEmail(employeeDto.getEmail()) || clientRepository.existsByEmail(employeeDto.getEmail())) {
            throw new AlreadyExistException("User with email " + employeeDto.getEmail() + " already exists");
        }

        Employee employee = employeeMapper.mapFromDto(employeeDto);
        employee.setId(null);
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.mapToDto(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }
}

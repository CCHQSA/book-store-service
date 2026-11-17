package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.mapper.Impl.EmployeeMapper;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void addEmployeeEncodesPasswordBeforeSaving() {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .email("employee@example.com")
                .password("password")
                .build();
        Employee employee = Employee.builder()
                .email(employeeDTO.getEmail())
                .password(employeeDTO.getPassword())
                .build();
        when(employeeRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(false);
        when(clientRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(false);
        when(employeeMapper.mapFromDto(employeeDTO)).thenReturn(employee);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(employeeMapper.mapToDto(employee)).thenReturn(employeeDTO);

        assertSame(employeeDTO, employeeService.addEmployee(employeeDTO));

        ArgumentCaptor<Employee> savedEmployee = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(savedEmployee.capture());
        assertEquals("encoded-password", savedEmployee.getValue().getPassword());
    }

    @Test
    void addEmployeeRejectsEmailUsedByAClient() {
        EmployeeDTO employeeDTO = EmployeeDTO.builder().email("client@example.com").build();
        when(employeeRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(false);
        when(clientRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> employeeService.addEmployee(employeeDTO));

        verify(employeeRepository, never()).save(any(Employee.class));
    }
}

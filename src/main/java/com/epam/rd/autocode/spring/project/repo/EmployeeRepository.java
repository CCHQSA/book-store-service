package com.epam.rd.autocode.spring.project.repo;

import com.epam.rd.autocode.spring.project.model.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(@NotBlank(message = "Email is required") @Email(message = "Please enter a valid email address") String email);

    boolean existsByEmail(String email);
}

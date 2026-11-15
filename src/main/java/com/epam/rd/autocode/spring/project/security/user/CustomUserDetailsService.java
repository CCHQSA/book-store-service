package com.epam.rd.autocode.spring.project.security;

import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.BlockedClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final BlockedClientRepository blockedClientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepository.findByEmail(username);
        if (employee.isPresent()) {
            return new CustomUserDetails(employee.get());
        }

        Optional<Client> client = clientRepository.findByEmail(username);
        if (client.isPresent()) {
            return new CustomUserDetails(client.get(), blockedClientRepository.existsByClientEmail(username));
        }

        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}

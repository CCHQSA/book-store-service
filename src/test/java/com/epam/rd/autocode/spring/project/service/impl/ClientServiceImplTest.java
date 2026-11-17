package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.mapper.Impl.ClientMapper;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.BlockedClientRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private BlockedClientRepository blockedClientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void addClientEncodesPasswordAndDefaultsBalanceToZero() {
        ClientDTO clientDTO = ClientDTO.builder()
                .email("client@example.com")
                .password("password")
                .name("Client")
                .build();
        Client client = Client.builder()
                .email(clientDTO.getEmail())
                .password(clientDTO.getPassword())
                .name(clientDTO.getName())
                .build();
        when(clientRepository.existsByEmail(clientDTO.getEmail())).thenReturn(false);
        when(employeeRepository.existsByEmail(clientDTO.getEmail())).thenReturn(false);
        when(clientMapper.mapFromDto(clientDTO)).thenReturn(client);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(clientMapper.mapToDto(client)).thenReturn(clientDTO);

        assertSame(clientDTO, clientService.addClient(clientDTO));

        ArgumentCaptor<Client> savedClient = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(savedClient.capture());
        assertEquals("encoded-password", savedClient.getValue().getPassword());
        assertEquals(BigDecimal.ZERO, savedClient.getValue().getBalance());
    }

    @Test
    void addClientRejectsAnExistingEmail() {
        ClientDTO clientDTO = ClientDTO.builder().email("client@example.com").build();
        when(clientRepository.existsByEmail(clientDTO.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> clientService.addClient(clientDTO));

        verify(clientRepository, never()).save(any(Client.class));
    }
}

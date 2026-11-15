package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.ClientAccountDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.mapper.Impl.ClientMapper;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.BlockedClient;
import com.epam.rd.autocode.spring.project.repo.BlockedClientRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final BlockedClientRepository blockedClientRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients() {
        return clientMapper.mapToDtoList(clientRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO getClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found with email: " + email));
        return clientMapper.mapToDto(client);
    }

    @Override
    @Transactional
    public ClientDTO updateClientByEmail(String email, ClientDTO clientDto) {
        Client currClient = clientRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found with email: " + email));

        currClient.setName(clientDto.getName());
        if (clientDto.getBalance() != null) {
            currClient.setBalance(clientDto.getBalance());
        }

        if (clientDto.getPassword() != null && !clientDto.getPassword().isBlank()) {
            currClient.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        }

        Client savedClient = clientRepository.save(currClient);
        return clientMapper.mapToDto(savedClient);
    }

    @Override
    @Transactional
    public void deleteClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found with email: " + email));

        blockedClientRepository.deleteByClientEmail(email);
        orderRepository.deleteByClientEmail(email);
        clientRepository.delete(client);
    }

    @Override
    @Transactional
    public ClientDTO addClient(ClientDTO clientDto) {
        if (existsByEmail(clientDto.getEmail()) || employeeRepository.existsByEmail(clientDto.getEmail())) {
            throw new AlreadyExistException("User with email " + clientDto.getEmail() + " already exists");
        }

        Client client = clientMapper.mapFromDto(clientDto);
        client.setId(null);
        client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        if (client.getBalance() == null) {
            client.setBalance(BigDecimal.ZERO);
        }
        Client savedClient = clientRepository.save(client);
        return clientMapper.mapToDto(savedClient);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Transactional(readOnly = true)
    public List<ClientAccountDTO> getAllClientAccounts() {
        Set<String> blockedEmails = blockedClientRepository.findAll().stream()
                .map(blockedClient -> blockedClient.getClient().getEmail())
                .collect(Collectors.toSet());
        return clientRepository.findAll().stream()
                .map(client -> new ClientAccountDTO(
                        client.getEmail(),
                        client.getName(),
                        client.getBalance(),
                        blockedEmails.contains(client.getEmail())))
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Transactional
    public void updateBlockedStatus(String email, boolean blocked) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found with email: " + email));
        boolean currentlyBlocked = blockedClientRepository.existsByClientEmail(email);
        if (blocked && !currentlyBlocked) {
            blockedClientRepository.save(new BlockedClient(client));
        } else if (!blocked && currentlyBlocked) {
            blockedClientRepository.deleteByClientEmail(email);
        }
    }
}

package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.ClientAccountDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getAllClients();

    ClientDTO getClientByEmail(String email);

    ClientDTO updateClientByEmail(String email, ClientDTO client);

    void deleteClientByEmail(String email);

    ClientDTO addClient(ClientDTO client);

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Please enter a valid email address") String email);

    List<ClientAccountDTO> getAllClientAccounts();

    void updateBlockedStatus(String email, boolean blocked);
}

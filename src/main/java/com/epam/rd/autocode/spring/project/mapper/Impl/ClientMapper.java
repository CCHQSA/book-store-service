package com.epam.rd.autocode.spring.project.mapper.Impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.mapper.BaseMapper;
import com.epam.rd.autocode.spring.project.model.Client;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ClientMapper implements BaseMapper<Client, ClientDTO> {

    @Override
    public Client mapFromDto(ClientDTO clientDTO) {
        if (clientDTO == null) {
            return null;
        }
        return Client.builder()
                .email(clientDTO.getEmail())
                .password(clientDTO.getPassword())
                .name(clientDTO.getName())
                .balance(clientDTO.getBalance() != null ? clientDTO.getBalance() : BigDecimal.ZERO)
                .build();
    }

    @Override
    public ClientDTO mapToDto(Client client) {
        if (client == null) {
            return null;
        }
        return ClientDTO.builder()
                .email(client.getEmail())
                .name(client.getName())
                .balance(client.getBalance())
                .build();
    }
}

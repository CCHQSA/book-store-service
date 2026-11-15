package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.LoginDtoRequest;

public interface AuthService {

    String login(LoginDtoRequest loginRequest);

    ClientDTO register(ClientDTO clientDTO);
}

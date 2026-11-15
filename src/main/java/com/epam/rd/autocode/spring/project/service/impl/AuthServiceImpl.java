package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.LoginDtoRequest;
import com.epam.rd.autocode.spring.project.security.jwt.JwtService;
import com.epam.rd.autocode.spring.project.service.AuthService;
import com.epam.rd.autocode.spring.project.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ClientService clientService;

    @Override
    public String login(LoginDtoRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtService.generateToken(userDetails);
    }

    @Override
    public ClientDTO register(ClientDTO clientDTO) {
        return clientService.addClient(clientDTO);
    }

}

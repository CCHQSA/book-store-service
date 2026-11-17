package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.LoginDtoRequest;
import com.epam.rd.autocode.spring.project.security.jwt.JwtService;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private ClientService clientService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void loginAuthenticatesUserAndReturnsJwt() {
        LoginDtoRequest loginRequest = new LoginDtoRequest("client@example.com", "password");
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        UserDetails userDetails = org.mockito.Mockito.mock(UserDetails.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");

        assertEquals("jwt-token", authService.login(loginRequest));

        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    void registerDelegatesToClientService() {
        ClientDTO clientDTO = ClientDTO.builder().email("client@example.com").build();
        when(clientService.addClient(clientDTO)).thenReturn(clientDTO);

        assertSame(clientDTO, authService.register(clientDTO));

        verify(clientService).addClient(clientDTO);
    }
}

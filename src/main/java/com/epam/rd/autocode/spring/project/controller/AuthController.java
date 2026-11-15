package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.LoginDtoRequest;
import com.epam.rd.autocode.spring.project.dto.RegisterDtoRequest;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String viewLogin(Model model) {
        model.addAttribute("loginRequest", new LoginDtoRequest("", ""));
        return "login";
    }

    @GetMapping("/register")
    public String viewRegister(Model model) {
        model.addAttribute("registerRequest", new RegisterDtoRequest("", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerRequest") RegisterDtoRequest registerDtoRequest,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            ClientDTO newClient = ClientDTO.builder()
                    .name(registerDtoRequest.getName())
                    .email(registerDtoRequest.getEmail())
                    .password(registerDtoRequest.getPassword())
                    .balance(BigDecimal.ZERO)
                    .build();

            authService.register(newClient);

            LoginDtoRequest loginRequest = new LoginDtoRequest(
                    registerDtoRequest.getEmail(),
                    registerDtoRequest.getPassword()
            );
            String jwt = authService.login(loginRequest);
            addJwtCookie(jwt, request, response);

            return "redirect:/home";
        } catch (AlreadyExistException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginRequest") LoginDtoRequest loginRequest,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            String jwt = authService.login(loginRequest);
            addJwtCookie(jwt, request, response);

            return "redirect:/home";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    private void addJwtCookie(String jwt, HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(request.isSecure())
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
    }
}

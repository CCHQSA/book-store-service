package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.dto.ProfileEditRequest;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ClientService clientService;
    private final EmployeeService employeeService;

    @GetMapping("/profile/delete")
    public String openDeleteProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        if (isEmployee(userDetails)) {
            model.addAttribute("employee", employeeService.getEmployeeByEmail(email));
        } else {
            model.addAttribute("client", clientService.getClientByEmail(email));
        }
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String viewEditProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        ProfileEditRequest form = new ProfileEditRequest();
        if (isEmployee(userDetails)) {
            EmployeeDTO employee = employeeService.getEmployeeByEmail(email);
            form.setName(employee.getName());
            form.setPhone(employee.getPhone());
            form.setBirthDate(employee.getBirthDate());
        } else {
            form.setName(clientService.getClientByEmail(email).getName());
        }

        model.addAttribute("email", email);
        model.addAttribute("profileEditRequest", form);
        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute("profileEditRequest") ProfileEditRequest form,
            BindingResult bindingResult,
            Model model) {
        String email = userDetails.getUsername();
        if (isEmployee(userDetails) && (form.getPhone() == null || form.getPhone().isBlank())) {
            bindingResult.rejectValue("phone", "required", "Phone number is required.");
        }
        if (isEmployee(userDetails) && form.getBirthDate() == null) {
            bindingResult.rejectValue("birthDate", "required", "Birth date is required.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("email", email);
            return "edit-profile";
        }

        if (isEmployee(userDetails)) {
            EmployeeDTO employee = EmployeeDTO.builder()
                    .email(email)
                    .name(form.getName())
                    .password(form.getPassword())
                    .phone(form.getPhone())
                    .birthDate(form.getBirthDate())
                    .build();
            employeeService.updateEmployeeByEmail(email, employee);
        } else {
            ClientDTO client = ClientDTO.builder()
                    .email(email)
                    .name(form.getName())
                    .password(form.getPassword())
                    .build();
            clientService.updateClientByEmail(email, client);
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request,
            HttpServletResponse response) {
        String email = userDetails.getUsername();
        clientService.deleteClientByEmail(email);

        ResponseCookie expiredJwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(request.isSecure())
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expiredJwtCookie.toString());
        return "redirect:/";
    }

    private boolean isEmployee(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_EMPLOYEE"));
    }
}

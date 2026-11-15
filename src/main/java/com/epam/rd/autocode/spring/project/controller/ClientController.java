package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/clients")
    public String viewClients(Model model) {
        model.addAttribute("clients", clientService.getAllClientAccounts());
        return "clients";
    }

    @PostMapping("/clients/{email}/block")
    public String blockClient(@PathVariable String email) {
        clientService.updateBlockedStatus(email, true);
        return "redirect:/clients";
    }

    @PostMapping("/clients/{email}/unblock")
    public String unblockClient(@PathVariable String email) {
        clientService.updateBlockedStatus(email, false);
        return "redirect:/clients";
    }
}

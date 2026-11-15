package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.service.BasketService;
import com.epam.rd.autocode.spring.project.service.BookService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;
    private final BookService bookService;

    @GetMapping("/basket/add")
    public String openAddToBasket(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        return "redirect:/books";
    }

    @GetMapping("/basket")
    public String viewBasket(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            Model model) {
        model.addAttribute("items", basketService.getItems(session, userDetails.getUsername()));
        return "basket";
    }

    @PostMapping("/basket/add")
    public String addToBasket(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute("bookItem") BookItemDTO bookItem,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please enter a valid quantity.");
            return "redirect:/books";
        }
        bookService.getBookByName(bookItem.getBookName());
        basketService.addItem(session, userDetails.getUsername(), bookItem);
        return "redirect:/basket";
    }

    @PostMapping("/basket/{name}/remove")
    public String removeFromBasket(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String name,
            HttpSession session) {
        basketService.removeItem(session, userDetails.getUsername(), name);
        return "redirect:/basket";
    }
}

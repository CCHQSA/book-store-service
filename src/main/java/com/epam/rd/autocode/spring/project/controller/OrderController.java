package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.BasketService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final BasketService basketService;

    @GetMapping("/orders/my")
    public String viewMyOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("orders", orderService.getOrdersByClient(userDetails.getUsername()));
        model.addAttribute("employeeView", false);
        return "orders";
    }

    @GetMapping("/orders")
    public String viewOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("orders", orderService.getOrdersForEmployee(userDetails.getUsername()));
        model.addAttribute("employeeView", true);
        return "orders";
    }

    @PostMapping("/orders/submit")
    public String submitOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session) {
        var items = basketService.getItems(session, userDetails.getUsername());
        if (items.isEmpty()) {
            throw new IllegalArgumentException("The basket is empty");
        }

        OrderDTO order = OrderDTO.builder()
                .clientEmail(userDetails.getUsername())
                .bookItems(items)
                .build();
        orderService.addOrder(order);
        basketService.clear(session, userDetails.getUsername());
        return "redirect:/orders/my";
    }

    @PostMapping("/orders/{id}/confirm")
    public String confirmOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        orderService.confirmOrder(id, userDetails.getUsername());
        return "redirect:/orders";
    }
}

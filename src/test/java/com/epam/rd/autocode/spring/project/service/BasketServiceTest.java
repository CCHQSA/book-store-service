package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BasketServiceTest {

    private final BasketService basketService = new BasketService();

    @Test
    void addItemCombinesQuantitiesForSameBook() {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("basket:client@example.com"))
                .thenReturn(List.of(new BookItemDTO("Book", 2)));

        basketService.addItem(session, "client@example.com", new BookItemDTO("Book", 3));

        verify(session).setAttribute("basket:client@example.com", List.of(new BookItemDTO("Book", 5)));
    }

    @Test
    void basketIsScopedToClient() {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("basket:client@example.com"))
                .thenReturn(List.of(new BookItemDTO("Book", 1)));

        assertEquals(1, basketService.getItems(session, "client@example.com").size());
        assertEquals(0, basketService.getItems(session, "other@example.com").size());
    }
}

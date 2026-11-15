package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasketService {

    public List<BookItemDTO> getItems(HttpSession session, String clientEmail) {
        Object value = session.getAttribute(attributeName(clientEmail));
        if (value == null) {
            return new ArrayList<>();
        }
        if (!(value instanceof List<?> storedItems)) {
            throw new IllegalStateException("Invalid basket data");
        }

        List<BookItemDTO> items = new ArrayList<>();
        for (Object storedItem : storedItems) {
            if (!(storedItem instanceof BookItemDTO item)) {
                throw new IllegalStateException("Invalid basket item");
            }
            items.add(new BookItemDTO(item.getBookName(), item.getQuantity()));
        }
        return items;
    }

    public void addItem(HttpSession session, String clientEmail, BookItemDTO item) {
        List<BookItemDTO> items = getItems(session, clientEmail);
        for (BookItemDTO current : items) {
            if (current.getBookName().equals(item.getBookName())) {
                current.setQuantity(current.getQuantity() + item.getQuantity());
                session.setAttribute(attributeName(clientEmail), items);
                return;
            }
        }
        items.add(new BookItemDTO(item.getBookName(), item.getQuantity()));
        session.setAttribute(attributeName(clientEmail), items);
    }

    public void removeItem(HttpSession session, String clientEmail, String bookName) {
        List<BookItemDTO> items = getItems(session, clientEmail);
        items.removeIf(item -> item.getBookName().equals(bookName));
        session.setAttribute(attributeName(clientEmail), items);
    }

    public void clear(HttpSession session, String clientEmail) {
        session.removeAttribute(attributeName(clientEmail));
    }

    private String attributeName(String clientEmail) {
        return "basket:" + clientEmail;
    }
}

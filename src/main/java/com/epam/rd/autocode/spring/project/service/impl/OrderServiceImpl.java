package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.dto.OrderViewDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.BookItem;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;

    @Override
    @PreAuthorize("hasRole('CLIENT')")
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByClient(String clientEmail) {
        return orderRepository.findAllByClientEmailOrderByOrderDateDesc(clientEmail).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByEmployee(String employeeEmail) {
        return orderRepository.findAllByEmployeeIsNullOrEmployeeEmailOrderByOrderDateDesc(employeeEmail).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Transactional(readOnly = true)
    public List<OrderViewDTO> getOrdersForEmployee(String employeeEmail) {
        return orderRepository.findAllByOrderByOrderDateDesc().stream()
                .map(order -> new OrderViewDTO(order.getId(), mapToDto(order)))
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('CLIENT')")
    public OrderDTO addOrder(OrderDTO orderDTO) {
        if (orderDTO.getBookItems() == null || orderDTO.getBookItems().isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least one book");
        }

        Client client = clientRepository.findByEmail(orderDTO.getClientEmail())
                .orElseThrow(() -> new NotFoundException("Client not found"));
        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDateTime.now());
        order.setPrice(BigDecimal.ZERO);
        order.setBookItems(new ArrayList<>());

        for (BookItemDTO requestedItem : orderDTO.getBookItems()) {
            if (requestedItem.getQuantity() == null || requestedItem.getQuantity() < 1) {
                throw new IllegalArgumentException("Book quantity must be positive");
            }
            Book book = bookRepository.findByName(requestedItem.getBookName())
                    .orElseThrow(() -> new NotFoundException("Book not found: " + requestedItem.getBookName()));
            BookItem item = new BookItem();
            item.setBook(book);
            item.setQuantity(requestedItem.getQuantity());
            item.setOrder(order);
            order.getBookItems().add(item);
            order.setPrice(order.getPrice().add(book.getPrice()
                    .multiply(BigDecimal.valueOf(requestedItem.getQuantity()))));
        }

        if (client.getBalance().compareTo(order.getPrice()) < 0) {
            throw new IllegalArgumentException("Insufficient balance to place this order");
        }
        client.setBalance(client.getBalance().subtract(order.getPrice()));
        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public OrderDTO confirmOrder(Long orderId, String employeeEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        if (order.getEmployee() != null) {
            throw new IllegalArgumentException("Order has already been confirmed");
        }
        Employee employee = employeeRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new NotFoundException("Employee not found"));
        order.setEmployee(employee);
        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }

    private OrderDTO mapToDto(Order order) {
        List<BookItemDTO> items = order.getBookItems().stream()
                .map(item -> new BookItemDTO(item.getBook().getName(), item.getQuantity()))
                .toList();
        return OrderDTO.builder()
                .clientEmail(order.getClient().getEmail())
                .employeeEmail(order.getEmployee() == null ? null : order.getEmployee().getEmail())
                .orderDate(order.getOrderDate())
                .price(order.getPrice())
                .bookItems(items)
                .build();
    }
}

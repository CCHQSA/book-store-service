package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.dto.OrderViewDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void addOrderCalculatesPriceAndDeductsClientBalance() {
        Client client = new Client(1L, "client@example.com", "hash", "Client", new BigDecimal("100"));
        Book book = new Book();
        book.setName("Book");
        book.setPrice(new BigDecimal("12.50"));
        when(clientRepository.findByEmail(client.getEmail())).thenReturn(Optional.of(client));
        when(bookRepository.findByName("Book")).thenReturn(Optional.of(book));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderDTO result = orderService.addOrder(OrderDTO.builder()
                .clientEmail(client.getEmail())
                .bookItems(List.of(new BookItemDTO("Book", 2)))
                .build());

        assertEquals(new BigDecimal("25.00"), result.getPrice());
        assertEquals(new BigDecimal("75.00"), client.getBalance());
        assertNotNull(result.getOrderDate());
        assertEquals(2, result.getBookItems().get(0).getQuantity());
    }

    @Test
    void addOrderRejectsInsufficientBalance() {
        Client client = new Client(1L, "client@example.com", "hash", "Client", new BigDecimal("10"));
        Book book = new Book();
        book.setName("Book");
        book.setPrice(new BigDecimal("12.50"));
        when(clientRepository.findByEmail(client.getEmail())).thenReturn(Optional.of(client));
        when(bookRepository.findByName("Book")).thenReturn(Optional.of(book));

        assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(OrderDTO.builder()
                .clientEmail(client.getEmail())
                .bookItems(List.of(new BookItemDTO("Book", 1)))
                .build()));
        assertEquals(new BigDecimal("10"), client.getBalance());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void confirmOrderAssignsEmployee() {
        Order order = new Order();
        order.setClient(new Client(1L, "client@example.com", "hash", "Client", BigDecimal.ZERO));
        Employee employee = new Employee();
        employee.setEmail("employee@example.com");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO result = orderService.confirmOrder(1L, employee.getEmail());

        assertEquals(employee.getEmail(), order.getEmployee().getEmail());
        assertEquals(employee.getEmail(), result.getEmployeeEmail());
    }

    @Test
    void employeeCanManageOrdersAssignedToAnyEmployee() {
        Client client = new Client(1L, "client@example.com", "hash", "Client", BigDecimal.ZERO);
        Employee otherEmployee = new Employee();
        otherEmployee.setEmail("other-employee@example.com");
        Order order = new Order();
        order.setId(1L);
        order.setClient(client);
        order.setEmployee(otherEmployee);
        order.setOrderDate(java.time.LocalDateTime.now());
        order.setPrice(BigDecimal.TEN);
        when(orderRepository.findAllByOrderByOrderDateDesc()).thenReturn(List.of(order));

        List<OrderViewDTO> result = orderService.getOrdersForEmployee("employee@example.com");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("other-employee@example.com", result.get(0).getOrder().getEmployeeEmail());
    }

    @Test
    void addOrderFailsWhenClientDoesNotExist() {
        when(clientRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.addOrder(OrderDTO.builder()
                .clientEmail("missing@example.com")
                .bookItems(List.of(new BookItemDTO("Book", 1)))
                .build()));
    }
}

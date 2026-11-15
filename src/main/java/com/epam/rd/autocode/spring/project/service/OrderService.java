package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.dto.OrderViewDTO;

import java.util.*;

public interface OrderService {

    List<OrderDTO> getOrdersByClient(String clientEmail);

    List<OrderDTO> getOrdersByEmployee(String employeeEmail);

    OrderDTO addOrder(OrderDTO order);

    OrderDTO confirmOrder(Long orderId, String employeeEmail);

    List<OrderViewDTO> getOrdersForEmployee(String employeeEmail);
}

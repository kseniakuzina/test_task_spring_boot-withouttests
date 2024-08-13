package com.test.controller;

import com.test.dto.OrderDTO;
import com.test.entity.Order;
import com.test.entity.Product;
import com.test.service.OrderService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public Pair<Order, List<Product>> createOrder(@RequestBody OrderDTO dto) {
        return orderService.createOrder(dto);
    }

    @GetMapping
    public List<Pair<Order, List<Product>>> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Pair<Order, List<Product>> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/{date}/{amount}")
    public Pair<Order, List<Product>> getOrdersByDateAndAmount(@PathVariable String date,@PathVariable Double amount) {
        return orderService.getOrderByDateAndAmount(date,amount);
    }

    @GetMapping("/{article_number}/{dateFrom}/{dateTo}")
    public List<Pair<Order, List<Product>>> getOrdersByProductAndPeriod(@PathVariable Long article_number, @PathVariable String dateFrom, @PathVariable String dateTo) {
        return orderService.getOrdersByProductAndPeriod(article_number,dateFrom,dateTo);
    }


}
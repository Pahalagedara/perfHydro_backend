package com.pira.orderservice.controller;

import com.pira.orderservice.dto.InventoryResponse;
import com.pira.orderservice.dto.OrderRequest;
import com.pira.orderservice.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        log.info(orderRequest.toString());
        return orderService.placeOrder(orderRequest);
    }
}

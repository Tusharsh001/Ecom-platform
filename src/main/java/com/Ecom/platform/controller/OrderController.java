package com.Ecom.platform.controller;


import com.Ecom.platform.model.dto.OrderRequest;
import com.Ecom.platform.model.dto.OrderResponse;
import com.Ecom.platform.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @PostMapping("/orders/place")
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse=orderService.placeOrder(orderRequest);
        return orderResponse;
    }


    @GetMapping("/orders")
    public List<OrderResponse> getAllOrderResponses(){
        return orderService.getAllOrderResponses();
    }

}

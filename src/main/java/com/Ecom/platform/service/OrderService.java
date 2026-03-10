package com.Ecom.platform.service;

import com.Ecom.platform.model.Orders;
import com.Ecom.platform.model.OrderItem;
import com.Ecom.platform.model.Product;
import com.Ecom.platform.model.dto.OrderItemRequest;
import com.Ecom.platform.model.dto.OrderItemResponse;
import com.Ecom.platform.model.dto.OrderRequest;
import com.Ecom.platform.model.dto.OrderResponse;
import com.Ecom.platform.repo.EcomRepo;
import com.Ecom.platform.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private EcomRepo Erepo;
    @Autowired
    private OrderRepo orderRepo;


    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Orders order=new Orders();
        String OrderId= UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(OrderId);
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems=new ArrayList<>();
        for(OrderItemRequest itemReq: orderRequest.items() ){
            Product product= Erepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product not Found"));
            product.setStockQuantity(product.getStockQuantity()-itemReq.quantity());
            Erepo.save(product);

            OrderItem orderItem=new OrderItem(product,itemReq.quantity(),product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())),order);
            orderItems.add(orderItem);
        }

        order.setOderItems(orderItems);
        Orders savedOrder=orderRepo.save(order);


        List<OrderItemResponse> itemResponses=new ArrayList<>();
        for(OrderItem item: order.getOderItems()){
            OrderItemResponse orderItemResponse=new OrderItemResponse(item.getProduct().getName(),
                                           item.getQuantity(),
                                           item.getTotalPrice());
            itemResponses.add(orderItemResponse);
        }
        OrderResponse orderResponse=new OrderResponse(savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                itemResponses);
        return orderResponse;
    }



    public List<OrderResponse> getAllOrderResponses() {
        return null;
    }
}

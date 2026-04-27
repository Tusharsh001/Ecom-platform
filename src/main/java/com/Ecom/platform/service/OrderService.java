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
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional
public class OrderService {

    @Autowired
    private EcomRepo Erepo;
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private VectorStore vectorStore;


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
            Product Savedproduct=Erepo.save(product);
            String filter=String.format("productId==%s", String.valueOf(product.getId()));
            vectorStore.delete(filter);
            String Content=String.format("""
                Product Name: %s
                Description: %s
                Brand: %s
                Category: %s
                Price: %.2f
                Release Date: %s
                Strock: %d
                """,
                    Savedproduct.getName(),
                    Savedproduct.getDescription(),
                    Savedproduct.getBrand(),
                    Savedproduct.getCategory(),
                    Savedproduct.getPrice(),
                    Savedproduct.getReleaseDate(),
                    Savedproduct.getStockQuantity());

            Document document=new Document(UUID.randomUUID().toString(),
                    Content,
                    Map.of("productId",String.valueOf(Savedproduct.getId())));
            vectorStore.add(List.of(document));


            OrderItem orderItem=new OrderItem(product,itemReq.quantity(),product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())),order);
            orderItems.add(orderItem);
        }

        order.setOderItems(orderItems);

        Orders savedOrder=orderRepo.save(order);

        StringBuilder content=new StringBuilder();
        content.append("Order Summary: \n");
        content.append("Order ID: ").append(savedOrder.getOrderId()).append("\n");
        content.append("Customer: ").append(savedOrder.getCustomerName()).append("\n");
        content.append("Email: ").append(savedOrder.getEmail()).append("\n");
        content.append("Date: ").append(savedOrder.getOrderDate()).append("\n");
        content.append("Status: ").append(savedOrder.getStatus()).append("\n");
        content.append("Products: ").append("\n");

        for(OrderItem orderItem: order.getOderItems() ){
            content.append("- ").append(orderItem.getProduct().getName())
                    .append(" x ").append(orderItem.getQuantity())
                    .append(" =$").append(orderItem.getTotalPrice()).append("\n");
        }

        Document document=new Document(
                UUID.randomUUID().toString(),
                content.toString(),
                Map.of("orderId",savedOrder.getId())
        );

        vectorStore.add(List.of(document));

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
        List<Orders> orders=orderRepo.findAll();
        List<OrderResponse> orderResponses=new ArrayList<>();
        for(Orders order: orders){
            List<OrderItemResponse> orderItemResponses=new ArrayList<>();
            List<OrderItem> items =order.getOderItems();
            for(OrderItem orderItem: items){
              OrderItemResponse response=new OrderItemResponse(orderItem.getProduct().getName(),
                      orderItem.getQuantity(),orderItem.getTotalPrice());
              orderItemResponses.add(response);
            }
            OrderResponse response=new OrderResponse(order.getOrderId(),
                    order.getCustomerName(),order.getEmail(),order.getStatus(),order.getOrderDate()
                    ,orderItemResponses);
            orderResponses.add(response);

        }
      return orderResponses;
    }
}

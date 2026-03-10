package com.Ecom.platform.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(unique = true)
    private String orderId;
    private String customerName;
    private String Email;
    private String status;
    private LocalDate orderDate;

    @OneToMany(mappedBy = "order" ,cascade = CascadeType.ALL)
    private List<OrderItem> oderItems;


    public Orders() {
    }

    public Orders(String orderId, String customerName, String email, String status, LocalDate orderDate, List<OrderItem> oderItems) {
        this.customerName = customerName;
        Email = email;
        this.oderItems = oderItems;
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.status = status;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Long  getId() {
        return id;
    }

    public void setId(Long  id) {
        this.id = id;
    }

    public List<OrderItem> getOderItems() {
        return oderItems;
    }

    public void setOderItems(List<OrderItem> oderItems) {
        this.oderItems = oderItems;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.Ecom.platform.repo;

import com.Ecom.platform.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Orders,Integer> {

    Optional<Orders> findByOrderId(String orderId);

}

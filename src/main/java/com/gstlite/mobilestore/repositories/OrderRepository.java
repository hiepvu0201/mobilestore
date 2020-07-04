package com.gstlite.mobilestore.repositories;

import com.gstlite.mobilestore.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    public Orders findByOrderName(String order);
}

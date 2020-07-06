package com.gstlite.mobilestore.repositories;

import com.gstlite.mobilestore.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin(origins = "*")
public interface OrderRepository extends JpaRepository<Orders, Long> {
    public Orders findByOrderName(String order);
}

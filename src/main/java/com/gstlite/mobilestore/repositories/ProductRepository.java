package com.gstlite.mobilestore.repositories;

import com.gstlite.mobilestore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public Product findByProductName(String productName);
}

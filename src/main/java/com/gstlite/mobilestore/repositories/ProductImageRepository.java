package com.gstlite.mobilestore.repositories;

import com.gstlite.mobilestore.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Repository
@CrossOrigin(origins = "*")
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}

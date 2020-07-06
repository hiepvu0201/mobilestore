package com.gstlite.mobilestore.repositories;

import com.gstlite.mobilestore.entities.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin(origins = "*")
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {
    public ProductGroup findByGroupName(String groupName);
}

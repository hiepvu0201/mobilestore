package com.gstlite.mobilestore.repositories;

import com.gstlite.mobilestore.entities.GroupVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupVariantRepository extends JpaRepository<GroupVariant, Long> {
    public GroupVariant findByVariantName(String variantName);
}

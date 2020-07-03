package com.gstlite.mobilestore.repositories;

import com.gstlite.mobilestore.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    public Users findByEmailAndPassword(String email, String password);
}

package com.gstlite.mobilestore.controllers;

import com.gstlite.mobilestore.entities.Users;
import com.gstlite.mobilestore.exceptions.ResourceNotFoundException;
import com.gstlite.mobilestore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {

        Users user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found on:" + userId));

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/add")
    public Users create(@Validated @RequestBody Users user){
        return userRepository.save(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Users> update(@PathVariable(value = "id") Long userId,
                                       @Validated @RequestBody Users userDetails) throws ResourceNotFoundException{

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on:" + userId));


        user.setPassword(userDetails.getPassword());
        final Users updateUser = userRepository.save(user);

        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long userId) throws Exception{
        Users user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found on:"+userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

    @PostMapping("/signin")
    public ResponseEntity<Users> signIn(@Validated @RequestBody Users u){
        Users user = userRepository.findByEmailAndPassword(u.getEmail(), u.getPassword());

        if(user==null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(user);
    }
}

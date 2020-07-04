package com.gstlite.mobilestore.controllers;

import com.gstlite.mobilestore.entities.Product;
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

import java.util.List;

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
    public Users create(@Validated @RequestBody Users user) throws Exception{
        String userEmail = user.getEmail();
        if(userEmail!=null&&!"".equals(userEmail)){
            Users tempUser = userRepository.findByEmail(userEmail);
            if(tempUser!=null){
                throw new Exception("user "+userEmail+" is already exist");
            }
        }
        return userRepository.save(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Users> update(@PathVariable(value = "id") Long userId,
                                       @Validated @RequestBody Users userDetails) throws ResourceNotFoundException, Exception{

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on:" + userId));

        boolean isdisable = user.isDisabled();
        if(isdisable==true){
            throw new Exception("user has already been disabled!");
        }
        user.setFullname(userDetails.getFullname());
        user.setPassword(userDetails.getPassword());
        final Users updateUser = userRepository.save(user);

        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/update-email/{id}")
    public ResponseEntity<Users> changePassword(@PathVariable(value = "id") Long userId,
                                                @Validated @RequestBody Users userDetails) throws ResourceNotFoundException, Exception{

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on:" + userId));

        boolean isdisable = user.isDisabled();
        if(isdisable==true){
            throw new Exception("user has already been disabled!");
        }

        Users tempEmail = userRepository.findByEmail(userDetails.getEmail());
        if(tempEmail!=null){
            throw new Exception("Email "+tempEmail+" is already exist");
        }

        user.setEmail(userDetails.getEmail());
        final Users updateUser = userRepository.save(user);

        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Users> disable(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException, Exception{

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on:" + userId));

        boolean isdisable = user.isDisabled();
        if(isdisable==true)
        {
            throw new Exception("user has already been disabled!");
        }
        user.setDisabled(true);
        final Users updateUser = userRepository.save(user);

        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Users> enable(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException, Exception{

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on:" + userId));

        boolean isdisable = user.isDisabled();
        if(isdisable==false)
        {
            throw new Exception("user has not been disabled yet!");
        }
        user.setDisabled(false);
        final Users updateUser = userRepository.save(user);

        return ResponseEntity.ok(updateUser);
    }

    @PostMapping("/signin")
    public ResponseEntity<Users> signIn(@Validated @RequestBody Users u) throws Exception{
        Users user = userRepository.findByEmailAndPassword(u.getEmail(), u.getPassword());
        boolean isdisable = user.isDisabled();
        if(isdisable==true){
            throw new Exception("Log in fail! This user has already been disabled!");
        }
        if(user==null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(user);
    }
}
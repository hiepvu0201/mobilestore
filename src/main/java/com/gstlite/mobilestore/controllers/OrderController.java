package com.gstlite.mobilestore.controllers;

import com.gstlite.mobilestore.entities.Orders;
import com.gstlite.mobilestore.entities.ProductGroup;
import com.gstlite.mobilestore.exceptions.ResourceNotFoundException;
import com.gstlite.mobilestore.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/list")
    public List<Orders> getAllOrders() {

        return orderRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Orders> getOrdersById(@PathVariable(value = "id") Long ordersId) throws ResourceNotFoundException {

        Orders orders = orderRepository.findById(ordersId).orElseThrow(()->new ResourceNotFoundException("Orders not found on:" + ordersId));

        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/add")
    public Orders create(@Validated @RequestBody Orders orders) throws Exception{
        String orderName = orders.getOrderName();
        if(orderName!=null&&!"".equals(orderName)){
            Orders tempOrdersName = orderRepository.findByOrderName(orderName);
            if(tempOrdersName!=null){
                throw new Exception("Orders "+orderName+" is already exist");
            }
        }
        return orderRepository.save(orders);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Orders> update(@PathVariable(value = "id") Long ordersId,
                                               @Validated @RequestBody Orders ordersDetails) throws ResourceNotFoundException, Exception{

        Orders orders = orderRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Orders not found on:" + ordersId));

        boolean isdisable = orders.isDisabled();
        if(isdisable==true){
            throw new Exception("Orders has already been disabled!");
        }
        orders.setAddress(ordersDetails.getAddress());
        orders.setCity(ordersDetails.getCity());
        orders.setZip(ordersDetails.getZip());
        orders.setStatus(ordersDetails.getStatus());
        orders.setComment(ordersDetails.getComment());
        orders.setTotalPrice(ordersDetails.getTotalPrice());
        orders.setType(ordersDetails.getType());

        final Orders updateOrders = orderRepository.save(orders);

        return ResponseEntity.ok(updateOrders);
    }

    @PutMapping("/update-name/{id}")
    public ResponseEntity<Orders> updateName(@PathVariable(value = "id") Long ordersId,
                                                   @Validated @RequestBody Orders ordersDetails) throws ResourceNotFoundException, Exception{

        Orders orders = orderRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Orders not found on:" + ordersId));

        boolean isdisable = orders.isDisabled();
        if(isdisable==true){
            throw new Exception("Orders has already been disabled!");
        }

        Orders tempOrders = orderRepository.findByOrderName(ordersDetails.getOrderName());
        if(tempOrders!=null){
            throw new Exception("Orders "+tempOrders+" is already exist");
        }

        orders.setOrderName(ordersDetails.getOrderName());
        final Orders updateOrders = orderRepository.save(orders);

        return ResponseEntity.ok(updateOrders);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Orders> disable(@PathVariable(value = "id") Long ordersId) throws ResourceNotFoundException, Exception{

        Orders orders = orderRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Orders not found on:" + ordersId));

        boolean isdisable = orders.isDisabled();
        if(isdisable==true)
        {
            throw new Exception("Orders has already been disabled!");
        }
        orders.setDisabled(true);
        final Orders updateOrders = orderRepository.save(orders);

        return ResponseEntity.ok(updateOrders);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Orders> enable(@PathVariable(value = "id") Long ordersId) throws ResourceNotFoundException, Exception{

        Orders orders = orderRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Orders not found on:" + ordersId));

        boolean isdisable = orders.isDisabled();
        if(isdisable==false)
        {
            throw new Exception("Orders has not been disabled yet!");
        }
        orders.setDisabled(false);
        final Orders updateOrders = orderRepository.save(orders);

        return ResponseEntity.ok(updateOrders);
    }
}

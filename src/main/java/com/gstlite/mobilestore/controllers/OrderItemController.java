package com.gstlite.mobilestore.controllers;

import com.gstlite.mobilestore.entities.OrderItem;
import com.gstlite.mobilestore.exceptions.ResourceNotFoundException;
import com.gstlite.mobilestore.repositories.GroupVariantRepository;
import com.gstlite.mobilestore.repositories.OrderItemRepository;
import com.gstlite.mobilestore.repositories.OrderRepository;
import com.gstlite.mobilestore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orderitem")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GroupVariantRepository groupVariantRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/list")
    public List<OrderItem> getAllOrderItem() {
        return orderItemRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable(value = "id") Long orderItemId) throws ResourceNotFoundException {

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(()->new ResourceNotFoundException("Order Item not found on:" + orderItemId));

        return ResponseEntity.ok().body(orderItem);
    }

    @PostMapping("/add")
    public OrderItem create(@Validated @RequestBody OrderItem orderItem) throws Exception{
        return orderItemRepository.save(orderItem);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrderItem> update(@PathVariable(value = "id") Long orderItemId,
                                         @Validated @RequestBody OrderItem orderItemDetails) throws ResourceNotFoundException, Exception{

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Item not found on:" + orderItemId));

        orderItem.setPrice(orderItemDetails.getPrice());
        orderItem.setProduct(productRepository.findById(orderItem.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + orderItem.getProductId())));
        orderItem.setGroupVariant(groupVariantRepository.findById(orderItem.getGroupVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Group Variant not found with id " + orderItem.getGroupVariantId())));
        orderItem.setOrders(orderRepository.findById(orderItem.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderItem.getOrderId())));

        final OrderItem updateOrderItem = orderItemRepository.save(orderItem);
        return ResponseEntity.ok(updateOrderItem);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long orderItemId) throws Exception {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
        .orElseThrow(() -> new ResourceNotFoundException("Order Item not found on: " + orderItemId));

        orderItemRepository.delete(orderItem);

        Map<String, Boolean> response = new HashMap<>();

        response.put("deleted", Boolean.TRUE);

        return response;
    }
}

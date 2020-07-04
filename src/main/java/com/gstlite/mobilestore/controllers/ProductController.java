package com.gstlite.mobilestore.controllers;

import com.gstlite.mobilestore.entities.Product;
import com.gstlite.mobilestore.exceptions.ResourceNotFoundException;
import com.gstlite.mobilestore.repositories.ProductGroupRepository;
import com.gstlite.mobilestore.repositories.ProductRepository;
import com.gstlite.mobilestore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductGroupRepository productGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public List<Product> getAllProduct() {

        return productRepository.findAll();
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long productId) throws ResourceNotFoundException {

        Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found on:" + productId));

        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/add")
    public Product create(@Validated @RequestBody Product product) throws Exception{
        String productName = product.getProductName();

        if(productName!=null&&!"".equals(productName)){
            Product tempProductName = productRepository.findByProductName(productName);
            if(tempProductName!=null){
                throw new Exception("Product  "+productName+" is already exist");
            }
        }
        product.setProductGroup(productGroupRepository.findById(product.getProductGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Group not found with id " + product.getProductGroupId())));
        product.setUsers(userRepository.findById(product.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + product.getUserId())));
        return productRepository.save(product);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> update(@PathVariable(value = "id") Long productId,
                                               @Validated @RequestBody Product productDetails) throws ResourceNotFoundException, Exception{

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found on:" + productId));

        boolean isdisable = product.isDisabled();
        if(isdisable==true){
            throw new Exception("Product has already been disabled!");
        }

        product.setUnitPrice(productDetails.getUnitPrice());
        product.setUnitInStock(productDetails.getUnitInStock());
        product.setDescription(productDetails.getDescription());
        product.setManufacturer(productDetails.getManufacturer());
        product.setProductCondition(productDetails.getProductCondition());
        product.setProductGroup(productGroupRepository.findById(product.getProductGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Group not found with id " + product.getProductGroupId())));
        product.setUsers(userRepository.findById(product.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + product.getUserId())));
        final Product updateProduct = productRepository.save(product);

        return ResponseEntity.ok(updateProduct);
    }

    //Coz we have set product name unique
    @PutMapping("/update-name/{id}")
    public ResponseEntity<Product> updateName(@PathVariable(value = "id") Long productId,
                                          @Validated @RequestBody Product productDetails) throws ResourceNotFoundException, Exception{

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found on:" + productId));

        boolean isdisable = product.isDisabled();
        if(isdisable==true){
            throw new Exception("Product has already been disabled!");
        }

        Product tempProductName = productRepository.findByProductName(productDetails.getProductName());
        if(tempProductName!=null){
            throw new Exception("Product  "+tempProductName+" is already exist");
        }

        product.setProductName(productDetails.getProductName());
        final Product updateProduct = productRepository.save(product);

        return ResponseEntity.ok(updateProduct);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Product> disable(@PathVariable(value = "id") Long productId) throws ResourceNotFoundException, Exception{

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found on:" + productId));

        boolean isdisable = product.isDisabled();
        if(isdisable==true)
        {
            throw new Exception("Product has already been disabled!");
        }
        product.setDisabled(true);
        final Product updateProduct = productRepository.save(product);

        return ResponseEntity.ok(updateProduct);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Product> enable(@PathVariable(value = "id") Long productId) throws ResourceNotFoundException, Exception{

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found on:" + productId));

        boolean isdisable = product.isDisabled();
        if(isdisable==false)
        {
            throw new Exception("Product has not been disabled yet!");
        }
        product.setDisabled(false);
        final Product updateProduct = productRepository.save(product);

        return ResponseEntity.ok(updateProduct);
    }
}

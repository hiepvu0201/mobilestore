package com.gstlite.mobilestore.controllers;

import com.gstlite.mobilestore.entities.Product;
import com.gstlite.mobilestore.entities.ProductImage;
import com.gstlite.mobilestore.exceptions.ResourceNotFoundException;
import com.gstlite.mobilestore.repositories.ProductImageRepository;
import com.gstlite.mobilestore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/productimage")
public class ProductImageController {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;



    @PostMapping("/upload")
    public ProductImage uploadImage(@RequestParam("myFile") MultipartFile file) throws IOException {

        List<Product> lst = productRepository.findAll();

        long id = 0;
        Product p = new Product();
        for (Product item: lst)
        {
            if(id < item.getId()) {
                p = item;
                id = item.getId();
            }
        }

        ProductImage img = new ProductImage(file.getBytes(), id, p);

        final ProductImage savedImage = productImageRepository.save(img);

        System.out.println("Image saved");

        return savedImage;
    }





    @PostMapping("/add")
    public ProductImage create(@RequestParam("myFile") MultipartFile file, @RequestBody long productId) throws IOException, ResourceNotFoundException {



        ProductImage productImage = new ProductImage();
        productImage.setPicByte(file.getBytes());
        productImage.setProductId(productId);
        productImage.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId)));
        return productImageRepository.save(productImage);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductImage> getProductImageById(@PathVariable(value = "id") Long productImageId) throws ResourceNotFoundException {

        ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(()->new ResourceNotFoundException("Product not found on:" + productImageId));

        return ResponseEntity.ok().body(productImage);
    }
    @GetMapping("/list")
    public List<ProductImage> getAll(){
        return productImageRepository.findAll();
    }
}
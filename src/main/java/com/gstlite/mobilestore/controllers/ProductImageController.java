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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/image")
public class ProductImageController {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    @PostMapping("/add")
    public ProductImage create(@Validated @RequestBody MultipartFile file, ProductImage productImage) throws IOException, ResourceNotFoundException {
        System.out.println("Original Image Byte Size - " + file.getBytes().length);
//        ProductImage img = new ProductImage(compressBytes(file.getBytes()), productImage.getProductId());

        productImage.setPicByte(compressBytes(file.getBytes()));
        productImage.setProduct(productRepository.findById(productImage.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productImage.getProductId())));
        return productImageRepository.save(productImage);
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

//    @GetMapping(path = { "/get/{imageName}" })
//    public ProductImage getImage(@PathVariable(value = "imageName") Long productId) throws IOException {
//        ProductImage productImage = productImageRepository.findById(productId);
//
//        ProductImage img = new ProductImage(decompressBytes(productImage.getPicByte()), productImage.getProductId(), productImage.getProduct());
//        return img;
//    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductImage> getProductById(@PathVariable(value = "id") Long productImageId) throws ResourceNotFoundException {

        ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(()->new ResourceNotFoundException("Product Image not found on:" + productImageId));

        ProductImage img = new ProductImage(productImage.getId(), decompressBytes(productImage.getPicByte()), productImage.getProductId(), productImage.getProduct(), productImage.isDisabled());

        return ResponseEntity.ok().body(img);
    }


}

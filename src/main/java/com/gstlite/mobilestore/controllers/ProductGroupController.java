package com.gstlite.mobilestore.controllers;

import com.gstlite.mobilestore.entities.ProductGroup;
import com.gstlite.mobilestore.entities.Users;
import com.gstlite.mobilestore.exceptions.ResourceNotFoundException;
import com.gstlite.mobilestore.repositories.ProductGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productgroup")
public class ProductGroupController {

    @Autowired
    private ProductGroupRepository productGroupRepository;

    @GetMapping("/list")
    public List<ProductGroup> getAllProductGroup() {

        return productGroupRepository.findAll();
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ProductGroup> getProductGroupById(@PathVariable(value = "id") Long productGroupId) throws ResourceNotFoundException {

        ProductGroup productGroup = productGroupRepository.findById(productGroupId).orElseThrow(()->new ResourceNotFoundException("Product Group not found on:" + productGroupId));

        return ResponseEntity.ok().body(productGroup);
    }

    @PostMapping("/add")
    public ProductGroup create(@Validated @RequestBody ProductGroup productGroup) throws Exception{
        String groupName = productGroup.getGroupName();
        if(groupName!=null&&!"".equals(groupName)){
            ProductGroup tempProductGroupName = productGroupRepository.findByGroupName(groupName);
            if(tempProductGroupName!=null){
                throw new Exception("Product Group "+groupName+" is already exist");
            }
        }
        return productGroupRepository.save(productGroup);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductGroup> update(@PathVariable(value = "id") Long productGroupId,
                                        @Validated @RequestBody ProductGroup productGroupDetails) throws ResourceNotFoundException, Exception{

        ProductGroup productGroup = productGroupRepository.findById(productGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Group not found on:" + productGroupId));

        boolean isdisable = productGroup.isDisabled();
        if(isdisable==true){
            throw new Exception("Product Group has already been disabled!");
        }
        productGroup.setPrice(productGroupDetails.getPrice());
        final ProductGroup updateProductGroup = productGroupRepository.save(productGroup);

        return ResponseEntity.ok(updateProductGroup);
    }

    @PutMapping("/update-name/{id}")
    public ResponseEntity<ProductGroup> updateName(@PathVariable(value = "id") Long productGroupId,
                                               @Validated @RequestBody ProductGroup productGroupDetails) throws ResourceNotFoundException, Exception{

        ProductGroup productGroup = productGroupRepository.findById(productGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Group not found on:" + productGroupId));

        boolean isdisable = productGroup.isDisabled();
        if(isdisable==true){
            throw new Exception("Product Group has already been disabled!");
        }

        ProductGroup tempProductGroup = productGroupRepository.findByGroupName(productGroupDetails.getGroupName());
        if(tempProductGroup!=null){
            throw new Exception("Product Group "+tempProductGroup+" is already exist");
        }

        productGroup.setGroupName(productGroupDetails.getGroupName());
        final ProductGroup updateProductGroup = productGroupRepository.save(productGroup);

        return ResponseEntity.ok(updateProductGroup);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<ProductGroup> disable(@PathVariable(value = "id") Long productGroupId) throws ResourceNotFoundException, Exception{

        ProductGroup productGroup = productGroupRepository.findById(productGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Group not found on:" + productGroupId));

        boolean isdisable = productGroup.isDisabled();
        if(isdisable==true)
        {
            throw new Exception("Product Group has already been disabled!");
        }
        productGroup.setDisabled(true);
        final ProductGroup updateProductGroup = productGroupRepository.save(productGroup);

        return ResponseEntity.ok(updateProductGroup);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<ProductGroup> enable(@PathVariable(value = "id") Long productGroupId) throws ResourceNotFoundException, Exception{

        ProductGroup productGroup = productGroupRepository.findById(productGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Group not found on:" + productGroupId));

        boolean isdisable = productGroup.isDisabled();
        if(isdisable==false)
        {
            throw new Exception("Product Group has not been disabled yet!");
        }
        productGroup.setDisabled(false);
        final ProductGroup updateProductGroup = productGroupRepository.save(productGroup);

        return ResponseEntity.ok(updateProductGroup);
    }
}

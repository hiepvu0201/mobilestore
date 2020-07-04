package com.gstlite.mobilestore.controllers;

import com.gstlite.mobilestore.entities.GroupVariant;
import com.gstlite.mobilestore.entities.ProductGroup;
import com.gstlite.mobilestore.exceptions.ResourceNotFoundException;
import com.gstlite.mobilestore.repositories.GroupVariantRepository;
import com.gstlite.mobilestore.repositories.ProductGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groupvariant")
public class GroupVariantController {

    @Autowired
    private GroupVariantRepository groupVariantRepository;

    @Autowired
    private ProductGroupRepository productGroupRepository;

    @GetMapping("/list")
    public List<GroupVariant> getAllGroupVariant() {

        return groupVariantRepository.findAll();
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<GroupVariant> getGroupVariantById(@PathVariable(value = "id") Long groupVariantId) throws ResourceNotFoundException {

        GroupVariant groupVariant = groupVariantRepository.findById(groupVariantId).orElseThrow(()->new ResourceNotFoundException("Group Variant not found on:" + groupVariantId));

        return ResponseEntity.ok().body(groupVariant);
    }

    @PostMapping("/add")
    public GroupVariant create(@Validated @RequestBody GroupVariant groupVariant) throws Exception{
        String variantName = groupVariant.getVariantName();
        if(variantName!=null&&!"".equals(variantName)){
            GroupVariant tempVariantName = groupVariantRepository.findByVariantName(variantName);
            if(tempVariantName!=null){
                throw new Exception("Group Variant "+variantName+" is already exist");
            }
        }
        groupVariant.setProductGroup(productGroupRepository.findById(groupVariant.getProductGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Group not found with id " + groupVariant.getProductGroupId())));
        return groupVariantRepository.save(groupVariant);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GroupVariant> update(@PathVariable(value = "id") Long groupVariantId,
                                                   @Validated @RequestBody GroupVariant groupVariantDetails) throws ResourceNotFoundException, Exception{

        GroupVariant groupVariant = groupVariantRepository.findById(groupVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("Group Variant not found on:" + groupVariantId));

        boolean isdisable = groupVariant.isDisabled();
        if(isdisable==true){
            throw new Exception("Group Variant has already been disabled!");
        }

        groupVariant.setProductGroup(productGroupRepository.findById(groupVariant.getProductGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Group not found with id " + groupVariant.getProductGroupId())));
        final GroupVariant updateGroupVariant = groupVariantRepository.save(groupVariant);

        return ResponseEntity.ok(updateGroupVariant);
    }

    @PutMapping("/update-name/{id}")
    public ResponseEntity<GroupVariant> updateName(@PathVariable(value = "id") Long groupVariantId,
                                                   @Validated @RequestBody GroupVariant groupVariantDetails) throws ResourceNotFoundException, Exception{

        GroupVariant groupVariant = groupVariantRepository.findById(groupVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("Group Variant not found on:" + groupVariantId));

        boolean isdisable = groupVariant.isDisabled();
        if(isdisable==true){
            throw new Exception("Group Variant has already been disabled!");
        }

        GroupVariant tempGroupVariant = groupVariantRepository.findByVariantName(groupVariantDetails.getVariantName());
        if(tempGroupVariant!=null){
            throw new Exception("Group Variant "+tempGroupVariant+" is already exist");
        }

        groupVariant.setVariantName(groupVariantDetails.getVariantName());
        final GroupVariant updateGroupVariant = groupVariantRepository.save(groupVariant);

        return ResponseEntity.ok(updateGroupVariant);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<GroupVariant> disable(@PathVariable(value = "id") Long groupVariantId) throws ResourceNotFoundException, Exception{

        GroupVariant groupVariant = groupVariantRepository.findById(groupVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("Group Variant not found on:" + groupVariantId));

        boolean isdisable = groupVariant.isDisabled();
        if(isdisable==true)
        {
            throw new Exception("Group Variant has already been disabled!");
        }
        groupVariant.setDisabled(true);
        final GroupVariant updateGroupVariant = groupVariantRepository.save(groupVariant);

        return ResponseEntity.ok(updateGroupVariant);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<GroupVariant> enable(@PathVariable(value = "id") Long groupVariantId) throws ResourceNotFoundException, Exception{

        GroupVariant groupVariant = groupVariantRepository.findById(groupVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("Group Variant not found on:" + groupVariantId));

        boolean isdisable = groupVariant.isDisabled();
        if(isdisable==false)
        {
            throw new Exception("Product Group has not been disabled yet!");
        }
        groupVariant.setDisabled(false);
        final GroupVariant updateGroupVariant = groupVariantRepository.save(groupVariant);

        return ResponseEntity.ok(updateGroupVariant);
    }
}

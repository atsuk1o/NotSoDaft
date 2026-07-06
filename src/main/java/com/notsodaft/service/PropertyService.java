package com.notsodaft.service;

import com.notsodaft.model.Property;
import com.notsodaft.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PropertyService{
    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository){
        this.propertyRepository = propertyRepository;
    }

    public List<Property> getVerifiedProperties(){
        return propertyRepository.findByVerifiedTrueAndActiveTrue();
    }

    public List<Property> getPendingProperties(){
        return propertyRepository.findByVerifiedFalse();
    }

    public Property save(Property property){
        return propertyRepository.save(property);
    }
}
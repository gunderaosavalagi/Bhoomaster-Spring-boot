package com.jsp.Bhoomaster.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.Bhoomaster.dto.Property;
import com.jsp.Bhoomaster.repository.PropertyRepository;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository; // Inject PropertyRepository

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
}


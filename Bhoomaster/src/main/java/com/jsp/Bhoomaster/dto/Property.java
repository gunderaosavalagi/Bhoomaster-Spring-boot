package com.jsp.Bhoomaster.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import jakarta.persistence.Transient; // Use JPA's @Transient annotation

@Data
@Entity
public class Property {
    
    @Id
    @GeneratedValue(generator = "property_id")
    @SequenceGenerator(name = "property_id", initialValue = 100121001, allocationSize = 1)
    private int id;
    @Lob
    private byte[] image;
    private String details;
    private String location;
    private int price; 
    private int year;
    private boolean approved;
    private String propertyName;
    
    public String getPropertyName() {
        return propertyName;
    }
    
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }    
    
    @ManyToOne
    private Builder builder;

    @Transient // Not persisted in the database, dynamically set in service
    private boolean booked;


}

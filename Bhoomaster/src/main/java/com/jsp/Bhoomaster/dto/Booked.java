package com.jsp.Bhoomaster.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Booked {

    @Id
    @GeneratedValue(generator = "booked_id")
    @SequenceGenerator(name = "booked_id", initialValue = 1, allocationSize = 1)
    private int id;

    @ManyToOne
    private User user; // Many bookings can belong to one user

    @OneToMany
    private List<Property> properties; // A booking can have multiple properties

    private String bookingDate;

    private double totalAmount; //  Total amount for booked properties
}


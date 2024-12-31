package com.jsp.Bhoomaster.service;

import com.jsp.Bhoomaster.dto.Booked;
import com.jsp.Bhoomaster.dto.Property;
import com.jsp.Bhoomaster.dto.User;
import com.jsp.Bhoomaster.repository.BookingRepository;
import com.jsp.Bhoomaster.repository.PropertyRepository;
import com.jsp.Bhoomaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    public String bookProperty(int userId, int propertyId) {
        // Fetch User
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        User user = userOpt.get();

        // Fetch Property
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        if (propertyOpt.isEmpty()) {
            throw new RuntimeException("Property not found with ID: " + propertyId);
        }
        Property property = propertyOpt.get();

        // Check if property is already booked
        if (isPropertyAlreadyBooked(user, property)) {
            return "Property is already booked by the user.";
        }

        // Create a new Booking
        Booked booked = new Booked();
        booked.setUser(user);
        booked.setProperties(List.of(property)); // Assuming the property is booked individually
        booked.setBookingDate(LocalDate.now().toString());
        booked.setTotalAmount(property.getPrice());

        // Save Booking
        bookingRepository.save(booked);

        return "Property booked successfully!";
    }

    private boolean isPropertyAlreadyBooked(User user, Property property) {
        return bookingRepository.existsByUserAndProperties(user, property);
    }

    public List<Booked> getUserBookedProperties(Integer userId) {
        // Fetch the user from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        // Retrieve the list of booked properties for the user
        return bookingRepository.findAllByUser(user);
    }

    // New Method: Fetch all properties with booking status
    public List<Property> getAllPropertiesWithBookingStatus() {
        List<Property> properties = propertyRepository.findAll(); // Fetch all properties
        List<Booked> bookedRecords = bookingRepository.findAll(); // Fetch all bookings

        // Determine the booking status for each property
        for (Property property : properties) {
            boolean isBooked = bookedRecords.stream()
                    .flatMap(booked -> booked.getProperties().stream())
                    .anyMatch(bookedProperty -> bookedProperty.getId() == property.getId());
            property.setBooked(isBooked); // Set the booking status
        }

        return properties;
    }
}

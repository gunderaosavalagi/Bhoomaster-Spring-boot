package com.jsp.Bhoomaster.repository;

import com.jsp.Bhoomaster.dto.Booked;
import com.jsp.Bhoomaster.dto.Property;
import com.jsp.Bhoomaster.dto.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booked, Integer> {

    boolean existsByUserAndProperties(User user, Property property);
    List<Booked> findAllByUser(User user);
}


package com.jsp.Bhoomaster.repository;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.Bhoomaster.dto.Builder;
import com.jsp.Bhoomaster.dto.Property; // Updated import

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {

    List<Property> findBybuilder(Builder builder);
    // You can define custom queries here if needed

    List<Property> findByBuilder(Builder builder);

    Optional<Property> findById(Long id);
}

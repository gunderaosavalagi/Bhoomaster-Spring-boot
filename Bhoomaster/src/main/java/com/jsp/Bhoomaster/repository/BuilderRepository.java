
package com.jsp.Bhoomaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.Bhoomaster.dto.Builder;

public interface BuilderRepository extends JpaRepository<Builder, Integer> {

	boolean existsByEmail(String email);

	boolean existsByMobile(Long mobile);

	Builder findByMobile(Long mobile);

	Builder findByEmail(String email);

}

package com.jsp.Bhoomaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.Bhoomaster.dto.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);

	boolean existsByMobile(Long mobile);

	User findByMobile(Long mobile);

	User findByEmail(String email);

}

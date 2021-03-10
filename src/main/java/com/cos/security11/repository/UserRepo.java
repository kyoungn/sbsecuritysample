package com.cos.security11.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security11.model.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	User findByUsername(String username);
}

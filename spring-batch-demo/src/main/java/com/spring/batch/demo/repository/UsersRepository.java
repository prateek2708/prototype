package com.spring.batch.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.batch.demo.entity.Users;


public interface UsersRepository extends JpaRepository<Users, Long> {
}

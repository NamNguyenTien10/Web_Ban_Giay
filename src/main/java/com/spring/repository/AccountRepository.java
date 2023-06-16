package com.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.entity.Accounts;

public interface AccountRepository  extends JpaRepository<Accounts, Integer>{
	Accounts findByUsernameAndPasswords(String username, String passwords);
}

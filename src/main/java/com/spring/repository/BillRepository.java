package com.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.entity.Accounts;
import com.spring.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer> {

	Bill findByAccount(Accounts account);

	Bill findByAccountId(Integer id);

	Bill getBillById(Integer billId);

}

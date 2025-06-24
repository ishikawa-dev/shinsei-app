package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.T_HEADER;

@Repository
public interface ExpenseIdRepository extends JpaRepository<T_HEADER,Object> {
	
	// ユーザーIDで検索するメソッド
	T_HEADER findByExpenseId(int expenseId);

}

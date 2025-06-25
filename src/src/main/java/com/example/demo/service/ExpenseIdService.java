package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.T_HEADER;
import com.example.demo.repository.ExpenseIdRepository;

@Service
public class ExpenseIdService {
	
	@Autowired
	private ExpenseIdRepository expenseIdRepository;

	//データベースからヘッダーの申請情報を取得するメソッド
	public T_HEADER getHeaderByExpenseId(int expenseId) {
		T_HEADER headerDatail = expenseIdRepository.findByExpenseId(expenseId);
		return headerDatail;
	}
}
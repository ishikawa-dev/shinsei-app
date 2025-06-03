package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.T_HEADER;
import com.example.demo.repository.HeaderRepository;

@Service
public class HeaderService {

	@Autowired
	private HeaderRepository headerRepository;
	
	public List<T_HEADER> getHeader(String userId) {
		
		//ログインIDからこれまでの申請履歴を取得する
		List<T_HEADER> headerList = headerRepository.findByUserId(userId);
		return headerList;
		
	}
}

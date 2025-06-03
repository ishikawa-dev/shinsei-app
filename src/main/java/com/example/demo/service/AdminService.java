package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.T_ADMIN_USER;
import com.example.demo.repository.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
    private AdminRepository AdminRepository;
	
	//ログインしたユーザー情報を取得するメソッド
	public T_ADMIN_USER getUserInfo(String userId, String password) {
		return AdminRepository.findByUserIdAndPassword(userId, password);
	}
	
	

}

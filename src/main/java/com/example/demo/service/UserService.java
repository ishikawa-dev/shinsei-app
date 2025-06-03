package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.T_USERS;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
    private UserRepository userRepository;
	
	// ========================================================
	// ユーザーIDとパスワードで検索するメソッド
	// ========================================================
	public T_USERS getUserInfo(String userId, String password) {
		return userRepository.findByUserIdAndPassword(userId, password);
	}
	
	// ========================================================
	// T_USERSに登録されている従業員一覧を取得する
	// ========================================================
   public List<T_USERS> employeeList(){
	   return userRepository.findAll();
   }
   
}

package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.T_ADMIN_USER;

@Repository
public interface AdminRepository extends JpaRepository<T_ADMIN_USER, String>  {

	//T_ADMIN_USERSに登録されているユーザー情報か検索するメソッド
	T_ADMIN_USER findByUserIdAndPassword(String userId, String password);
	
}

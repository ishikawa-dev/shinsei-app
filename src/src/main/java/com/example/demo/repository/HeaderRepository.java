package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.T_HEADER;

public interface HeaderRepository extends JpaRepository<T_HEADER,Object> {
	
	// ユーザーIDで検索するメソッド
	List<T_HEADER> findByUserId(String userId);

}

package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.T_USERS;

@Repository
public interface UserRepository extends JpaRepository<T_USERS, String> {

	// ========================================================
	// ユーザーIDとパスワードで検索するメソッド
	// ========================================================
	T_USERS findByUserIdAndPassword(String userId, String password);
	
	// ========================================================
	// T_USERSに登録されている従業員一覧を取得する
	// ========================================================
	List<T_USERS> findAll();
	
	// ========================================================
	// 申請IDを検索値としてユーザー名を取得する
	// ========================================================
    @Query("SELECT u.name FROM T_USERS u WHERE u.userId= :userId")
    String getNameById(@Param("userId") String userId);
}

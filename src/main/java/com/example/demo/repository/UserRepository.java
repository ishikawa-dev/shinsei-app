package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	List<T_USERS> findAllByOrderByUserIdAsc();
	
	// ========================================================
	// 申請IDを検索値としてユーザー名を取得する
	// ========================================================
    @Query("SELECT u.name FROM T_USERS u WHERE u.userId= :userId")
    String getNameById(@Param("userId") String userId);
    
	// ========================================================
	// ユーザーIDで検索するメソッド
	// ========================================================
	T_USERS findByUserId(String userId);
	
	// ========================================================
	// パスワードを新しいものに変更する
	// ========================================================
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_USERS SET password = :password WHERE userId = :userId")
	 int updatePassword(@Param("password") String password,@Param("userId") String userId);
}

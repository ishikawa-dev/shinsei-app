package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // getter, setter, toString, equals, hashCode を全部自動生成
@NoArgsConstructor // 引数なしコンストラクタ
@AllArgsConstructor // 全フィールドのコンストラクタ
@Table(name = "T_USERS")
public class T_USERS {
	
	@Id
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "name")
	private String name;
	
}

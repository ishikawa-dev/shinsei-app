package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // getter, setter, toString, equals, hashCode を全部自動生成
@NoArgsConstructor // 引数なしコンストラクタ
@AllArgsConstructor // 全フィールドのコンストラクタ
@Table(name = "T_HEADER")
public class T_HEADER {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "expense_id")
	private int expenseId;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "expense_month")
	private String expenseMonth;
	
	@Column(name = "created_at")
	private LocalDate createdDate;
	
	@Column(name = "submission_date")
	private LocalDate submissionDate;
	
	@Column(name = "payment_date")
	private LocalDate paymentDate;
	
	@Column(name = "approval_status")
	private int approvalStatus; 

	@Column(name = "expense_flag")
	private int expenseFlag;
	
	@Column(name = "dismissed_reason")
	private String dismissedReason;
	
	@Column(name = "display_flag")
	private int displayFlag;
	
	@Column(name = "expense_year")
	private int expenseYear;
	
	@Transient
	private String newExpenseMonth;
}
package com.example.demo.model;

import java.math.BigDecimal;
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
@Table(name = "T_DETAILS")
public class T_DETAILS {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "detail_id")
	private int detailId;
	
	@Column(name = "expense_id")
	private int expenseId;
	
	@Column(name = "usage_date")
	private LocalDate usageDate;
	
	@Column(name = "valid_from")
	private LocalDate validFrom;
	
	@Column(name = "valid_until")
	private LocalDate validUntil;
	
	@Column(name = "transportation_facilities")
	private String transportationFacilities;
	
	@Column(name = "route")
	private String route;
	
	@Column(name = "other_expense_item")
	private String otherExpenseItem;
	
	@Column(name = "round_trip_flag")
	private int roundTripFlag;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "purpose")
	private String purpose;
	
	@Column(name = "receipt")
	private String receipt;
	
	@Column(name = "receipt_uploaded_flag")
	private int receiptUploadedFlag;
	
	@Column(name = "expense_type")
	private int expenseType;
	
	@Transient
	private String temporaryId;
	
	@Column(name = "save_Flag")
	private int saveFlag;
	
	// 単発交通費用コンストラクタ
    public T_DETAILS(LocalDate usageDate, String transportationFacilities, String route,int roundTripFlag, BigDecimal amount, String purpose) {
        this.usageDate = usageDate;
        this.transportationFacilities = transportationFacilities;
        this.route = route;
        this.roundTripFlag = roundTripFlag;
        this.amount = amount;
        this.purpose = purpose;
    }
    
	// 定期交通費用コンストラクタ
    public T_DETAILS( LocalDate validFrom,LocalDate validUntil,String transportationFacilities, String route, BigDecimal amount, String purpose) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.transportationFacilities = transportationFacilities;
        this.route = route;
        this.amount = amount;
        this.purpose = purpose;
    }
    
	// 業務経費用コンストラクタ
    public T_DETAILS(LocalDate usageDate, String otherExpenseItem, BigDecimal amount, String purpose) {
        this.usageDate = usageDate;
        this.otherExpenseItem = otherExpenseItem;
        this.amount = amount;
        this.purpose = purpose;
    }

}

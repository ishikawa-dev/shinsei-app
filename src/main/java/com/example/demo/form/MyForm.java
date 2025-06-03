package com.example.demo.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.T_HEADER;

import lombok.Data;

@Data
public class MyForm {

	//ラジオボタンの選択された値（expenseId)を保持するフィールド
	private int radioOption;
	private int radioOptionID;   //明細ID用
	private List<Integer> checkboxID;
	
	//ラジオボタンで表示するためのヘッダーリスト
	private List<T_HEADER> headerList;
	
    private String expenseMonth;   // 例: 申請月
    private LocalDate createdDate;    // 例: 入力日
    private LocalDate submissionDate; // 例: 申請日
    private LocalDate paymentDate;    // 例: 支払い日
    private Integer approvalStatus; // 例: 承認ステータス（0:　承認　1: 却下）
    private Integer expenseFlag;		//申請フラグ (例: 0=未申請　1=申請済)
        
    private Integer detailId;		//明細ID
    private Integer expenseId;		//申請ID  
    private LocalDate usageDate;		//利用日付 (YYYYMMDD)
    private LocalDate validFrom;		//定期適用開始日
    private LocalDate validUntil;		//定期適用終了日
    private String transportationFacilities;		//利用交通機関
    private String route;		//利用区間
    private String otherExpenseItem;		//その他の項目
    private Integer roundTripFlag;		//往復か片道か(例: 1=片道, 2=往復)
    private BigDecimal amount;		//利用金額
    private String purpose;		//利用目的
    private MultipartFile receipt;		//領収書
    private Integer receiptUploadedFlag;		//領収書確認フラグ (例: 0=なし, 1=あり)
    private Integer displayFlag;		//表示フラグ (例: 1=表示　2=未表示)
    private Integer expenseType;		//経費種別（1=単発交通費　2=定期交通費　3=業務経費）
    
}

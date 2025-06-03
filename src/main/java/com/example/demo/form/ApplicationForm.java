package com.example.demo.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.T_DETAILS;
import com.example.demo.model.T_HEADER;

import lombok.Data;

@Data
public class ApplicationForm {

	private String radioOption;  // ラジオボタンで選択された値（申請IDなど）
	private int radioOptionID;  //詳細画面のラジオボタンで選択された値（明細ID）
	private List<Integer> checkboxID;
	
	//ラジオボタンで表示するためのヘッダーリスト
	private List<T_HEADER> headerList;
	
	//ラジオボタンで表示するためのヘッダーリスト
	private List<T_DETAILS> inputSingleTransport;
	private List<T_DETAILS> inputCommuteTransport;
	private List<T_DETAILS> inputBusinessExpense;
	private MultipartFile file;
	
	//モーダル保存時の一時ID
	private String temporaryId;
	//コピー申請用の申請タイトル
	private String newExpenseMonth;

    private String expenseMonth;   // 例: 申請月
    private LocalDate createdDate;    // 例: 入力日
    private LocalDate submissionDate; // 例: 申請日
    private LocalDate paymentDate;    // 例: 支払い日
    private Integer approvalStatus; // 例: 承認ステータス（0:　承認　1: 却下）
    private Integer expenseFlag;		//申請フラグ (例: 0=未申請　1=申請済)
    private Integer displayFlag;
    private String dismissedReason;
    private int expenseYear;
        
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
    private MultipartFile  receipt;		//領収書
    private Integer receiptUploadedFlag;		//領収書確認フラグ (例: 0=なし, 1=あり)
    private Integer expenseType;		//経費種別（1=単発交通費　2=定期交通費　3=業務経費）
    private Integer saveFlag;		//保存フラグ (例: 0=未保存　1=保存済)
    
}


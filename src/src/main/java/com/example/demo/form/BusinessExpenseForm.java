package com.example.demo.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BusinessExpenseForm {
        
    private Integer detailId;		//明細ID
    private Integer expenseId;		//申請ID
    
    @NotNull(message = "利用日は必須です")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate usageDate;		//利用日付 (YYYYMMDD)
    
    @Size(min = 1, message = "項目は必須です")
    private String otherExpenseItem;		//その他の項目
    
    @NotNull(message = "金額は必須です")
    private BigDecimal amount;		//利用金額
    
    @Size(min = 1, message = "利用目的は必須です")
    private String purpose;		//利用目的
    
    private MultipartFile  receipt;		//領収書
    
    private Integer receiptUploadedFlag;		//領収書確認フラグ (例: 0=なし, 1=あり)
    private Integer displayFlag;		//表示フラグ (例: 1=表示　2=未表示)
    private Integer expenseType;		//経費種別（1=単発交通費　2=定期交通費　3=業務経費）
    private Integer saveFlag;		//保存フラグ (例: 0=未保存　1=保存済)
    
    // ここからバリデーション用のメソッド追加
    @AssertTrue(message = "領収書は必須です")
    public boolean isReceiptValid() {
        return receipt != null && !receipt.isEmpty();
    }
}

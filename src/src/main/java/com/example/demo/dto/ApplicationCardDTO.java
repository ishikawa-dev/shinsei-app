package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplicationCardDTO {

	private int expenseId;
    private String expenseMonth;
    private LocalDate createdDate;
    private LocalDate submissionDate;
    private LocalDate paymentDate;
    private int approvalStatus;
    private BigDecimal totalAmount;
    private String userId;
    private String dismissedReason;
    private int expenseYear;
    
    // ネイティブクエリ用コンストラクタ（java.sql.Dateで受け取る）
    public ApplicationCardDTO(
        int expenseId,
        String expenseMonth,
        java.sql.Date createdDateSql,
        java.sql.Date submissionDateSql,
        java.sql.Date paymentDateSql,
        int approvalStatus,
        BigDecimal totalAmount,
        String userId,
        String dismissedReason,
        int expenseYear) {

        this.expenseId = expenseId;
        this.expenseMonth = expenseMonth;
        this.createdDate = createdDateSql != null ? createdDateSql.toLocalDate() : null;
        this.submissionDate = submissionDateSql != null ? submissionDateSql.toLocalDate() : null;
        this.paymentDate = paymentDateSql != null ? paymentDateSql.toLocalDate() : null;
        this.approvalStatus = approvalStatus;
        this.totalAmount = totalAmount;
        this.userId = userId;
        this.dismissedReason = dismissedReason;
        this.expenseYear = expenseYear;
	}
    
}

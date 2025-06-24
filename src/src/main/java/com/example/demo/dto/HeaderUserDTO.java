package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class HeaderUserDTO {

    private int expenseId;
    private String expenseMonth;
    private LocalDate submissionDate;
    private String name;
    private BigDecimal totalAmount;

    // ネイティブクエリ用コンストラクタ（java.sql.Dateで受け取る）
    public HeaderUserDTO(
        int expenseId,
        String expenseMonth,
        java.sql.Date submissionDateSql,
        String name,
        BigDecimal totalAmount) {

        this.expenseId = expenseId;
        this.expenseMonth = expenseMonth;
        this.submissionDate = submissionDateSql != null ? submissionDateSql.toLocalDate() : null;
        this.name = name;
        this.totalAmount = totalAmount;
        
    }
}
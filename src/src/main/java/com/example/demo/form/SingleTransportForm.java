package com.example.demo.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SingleTransportForm {
    
    @NotNull(message = "利用日は必須です")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate usageDate;		//利用日付 (YYYYMMDD)
    
    @NotBlank(message = "利用交通機関は必須です")
    private String transportationFacilities;		//利用交通機関
    
    @NotBlank(message = "利用区間は必須です")
    private String route;		//利用区間
    
    @NotNull(message = "選択は必須です")
    private Integer roundTripFlag;		//往復か片道か(例: 1=片道, 2=往復)
    
    @NotNull(message = "金額は必須です")
    private BigDecimal amount;		//利用金額
    
    private String purpose;		//利用目的
    private Integer displayFlag;		//表示フラグ (例: 1=表示　2=未表示)
    private Integer expenseType;		//経費種別（1=単発交通費　2=定期交通費　3=業務経費）
    private Integer saveFlag;		//保存フラグ (例: 0=未保存　1=保存済)
    
    private Integer detailId;
}

package com.example.demo.common;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.example.demo.model.T_DETAILS;
import com.example.demo.service.ApplicationService;

//★：申請内容の取得、合算金額の取得を請け負うクラス
@Component
public class DisplayInfoHelper{
	
	@Autowired
	private ApplicationService appService;

	public void setupDisplayInfo(Model model,HttpSession session,int expenseId ){
		
		//DBに登録した内容を取得し、セッションとmodelに保存する
		//単発交通費ver
		List<T_DETAILS> getSTDetails = appService.getDBDisplayInfo(expenseId,1);
	    session.setAttribute("getSTDetails", getSTDetails);
	    model.addAttribute("getSTDetails", getSTDetails);
	    //定期交通費ver
	    List<T_DETAILS> getCTDetails = appService.getDBDisplayInfo(expenseId,2);
	    session.setAttribute("getCTDetails", getCTDetails);
	    model.addAttribute("getCTDetails", getCTDetails);
	    //業務経費ver
	    List<T_DETAILS> getBEDetails = appService.getDBDisplayInfo(expenseId,3);
	    session.setAttribute("getBEDetails", getBEDetails);
	    model.addAttribute("getBEDetails", getBEDetails);
	    
		//合算金額の表示
		int totalAmount = appService.totalAmountInfo(expenseId);
		model.addAttribute("totalAmount", totalAmount);
	}
}

package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.ApplicationService;

@Controller
public class LogoutController {

	@Autowired
	private ApplicationService appService;
	
	@GetMapping("/logout")
	public String showLogout(HttpSession session) {
	    
	    String userId = (String)session.getAttribute("userId");
		
		//userIdを元に保存フラグ「０」の申請データをDB上から削除する
		List<String> expenseIds = appService.findExpenseIdsWithFlagZero(userId);
		int deleteBySaveFlagOfZero = appService.deleteBySaveFlagOfZero(expenseIds);
	    
		//セッションを全部破棄する
		session.invalidate();
		return "login/login";
	}
	
}

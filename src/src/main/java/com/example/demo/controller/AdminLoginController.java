package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.T_ADMIN_USER;
import com.example.demo.service.AdminService;
import com.example.demo.service.UserService;

@Controller
public class AdminLoginController {

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserService userService;
	
	//ログイン画面を表示
	@GetMapping("/admin/login")
	public String getLogin() {
		return "admin/login";
	}
	
	//ログインボタンが押下される
	@PostMapping("/admin/login")
    public String login(@RequestParam String userId,
    					@RequestParam String password,
    					Model model,HttpSession session) {

        T_ADMIN_USER loginUser = adminService.getUserInfo(userId, password);
        
        if (loginUser != null) {
            return "redirect:/admin/mypage";  // → mypage.html を表示
        } else {
            model.addAttribute("error", "ログインに失敗しました");
            return "admin/login";  // → ログイン画面に戻る
        }
	}
}

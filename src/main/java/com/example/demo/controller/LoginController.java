package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.T_USERS;
import com.example.demo.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	//ログイン画面を表示
	@GetMapping("/login")
	public String getLogin() {
		return "login/login";
	}
	
	//ログインボタンが押下される
	@PostMapping("/login")
    public String login(@RequestParam String userId,@RequestParam String password,Model model,HttpSession session) {

        T_USERS loginUser = userService.getUserInfo(userId, password);
        
        if (loginUser != null) {
        	// 名前をHTMLに渡す
            session.setAttribute("userName", loginUser.getName()); 
            session.setAttribute("userId", userId);
            return "redirect:/mypage";  // → mypage.html を表示
        } else {
            model.addAttribute("error", "ログインに失敗しました");
            return "login/login";  // → ログイン画面に戻る
        }
	}
}

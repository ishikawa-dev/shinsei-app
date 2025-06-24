package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//ログイン画面を表示
	@GetMapping("/login")
	public String getChangePass() {
		return "login/login";
	}
	
	//ログインボタンが押下される
	@PostMapping("/login")
    public String login(@RequestParam String userId,@RequestParam String password,Model model,HttpSession session) {
        
        // DBからパスワードは除外して取得
        T_USERS loginUser = userService.findByUserId(userId);

        if (loginUser != null && passwordEncoder.matches(password, loginUser.getPassword())) {
            session.setAttribute("userName", loginUser.getName()); 
            session.setAttribute("userId", userId);
            return "redirect:/mypage";
        } else {
            model.addAttribute("error", "ログインに失敗しました");
            return "login/login";
        }
        
	}
}
package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.form.UserForm;
import com.example.demo.model.T_USERS;
import com.example.demo.service.UserService;

@Controller
public class ChangePassController {
	
	@Autowired
	private UserService userService;

	//ログイン画面を表示
	@GetMapping("/login/changePass")
	public String getChangePass(Model model) {
		model.addAttribute("userForm",new UserForm());
		return "login/changePass";
	}
	
	//確定ボタンが押下される
	@PostMapping("/login/changePass")
    public String changePass(@ModelAttribute UserForm userForm,
    						 Model model,HttpSession session) {
		
		String userId = userForm.getUserId();
		String now_password = userForm.getNow_password();
		String new_password = userForm.getNew_password();
		String new_password_again = userForm.getNew_password_again();
		
		T_USERS user = userService.findByUserId(userId);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashed_user_pass = encoder.encode(now_password);
		
	    if (user == null) {
	        model.addAttribute("confirmMessage", "該当のユーザーがいません");
	        return "login/changePass";
	    }

	    if (!encoder.matches(now_password, hashed_user_pass)) {
	        model.addAttribute("confirmMessage", "現在のパスワードが正しくありません");
	        return "login/changePass";
	    }

	    if (!new_password.equals(new_password_again)) {
	        model.addAttribute("confirmMessage", "確認用パスワードが一致していません");
	        return "login/changePass";
	    }

	    // パスワード更新
	    String hashed = encoder.encode(new_password);
	    userService.updatePassword(hashed, user.getUserId());
	    model.addAttribute("confirmMessage", "パスワードの変更が完了しました");
		
		return "login/login";
	}

}

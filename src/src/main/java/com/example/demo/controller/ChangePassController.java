package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	
	@PostMapping("/login/changePass")
	public String changePass(
	    @Valid @ModelAttribute UserForm userForm,
	    BindingResult bindingResult,
	    Model model,
	    HttpSession session) {

	    if (bindingResult.hasErrors()) {
	        return "login/changePass";
	    }

	    String userId = userForm.getUserId();
	    String now_password = userForm.getNowPassword();
	    String new_password = userForm.getNewPassword();

	    T_USERS user = userService.findByUserId(userId);
	    if (user == null) {
	        model.addAttribute("confirmMessage", "該当のユーザーがいません");
	        return "login/changePass";
	    }

	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();	    
	    if (!encoder.matches(now_password, user.getPassword())) {
	        model.addAttribute("confirmMessage", "現在のパスワードが正しくありません");
	        return "login/changePass";
	    }
	    
	    if (!userForm.getNewPassword().equals(userForm.getNewPasswordAgain())) {
	    	model.addAttribute("confirmMessage", "新しいパスワードと確認用パスワードが一致しません");
	        return "login/changePass";
	    }
	    



	    // パスワード更新
	    String hashed = encoder.encode(new_password);
	    userService.updatePassword(hashed, user.getUserId());
	    model.addAttribute("confirmMessage", "パスワードの変更が完了しました");

	    return "login/login";
	}

}

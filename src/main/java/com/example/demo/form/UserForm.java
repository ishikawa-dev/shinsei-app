package com.example.demo.form;

import lombok.Data;

@Data
public class UserForm {
	
	private String userId;
	private String now_password;
	private String new_password;
	private String new_password_again;

}

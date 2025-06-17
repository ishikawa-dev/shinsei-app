package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class UserForm {

    @NotBlank(message = "※ユーザー名は必須です")
    private String userId;

    @NotBlank(message = "※現在のパスワードは必須です")
    private String nowPassword;

    @NotBlank(message = "※新しいパスワードは必須です")
    @Size(min = 8, message = "※パスワードは8文字以上で入力してください")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
             message = "※パスワードは英字と数字の両方を含めてください")
    private String newPassword;

    @NotBlank(message = "※確認用パスワードは必須です")
    private String newPasswordAgain;
    
    /*@AssertTrue(message = "新しいパスワードと確認用パスワードが一致しません")
    public boolean isNewPasswordMatching() {
        if (newPassword == null || newPasswordAgain == null) return false;
        return newPassword.equals(newPasswordAgain);
    }*/
}
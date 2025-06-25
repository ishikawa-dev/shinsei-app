package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ReceiptController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/uploadReceipt")
    public String handleFileUpload(@RequestParam("receiptFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            // フォルダが存在しない場合は作成
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            // ファイル名をユニークにする（例：タイムスタンプ付き）
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // 保存先のパス
            Path path = Paths.get(UPLOAD_DIR + fileName);

            // ファイル保存
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 必要なら、fileName をDBに保存する処理を追加
        }

        return "redirect:/mypage";  // 遷移先は適宜変更
    }
}
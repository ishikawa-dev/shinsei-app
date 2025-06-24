package com.example.demo.controller;

/*@RestController
public class CheckHeaderController {

	@PostMapping("/application/checkHeader")
	public String checkHeader(@ModelAttribute ApplicationForm appForm, HttpSession session) {

        // セッションに保存されている申請ヘッダー情報の存在確認
        T_HEADER headerInfo = (T_HEADER) session.getAttribute("savedHeader");
        if (headerInfo == null) {
            return "申請タイトルの情報が保存されていません。";
        }

        // 以下は各項目の入力チェック
        if (appForm.getValidFrom() == null) {
            return "定期適用開始日を入力してください。";
        }

        if (appForm.getValidUntil() == null) {
            return "定期適用終了日を入力してください。";
        }

        if (appForm.getTransportationFacilities() == null || appForm.getTransportationFacilities().isBlank()) {
            return "利用交通機関を入力してください。";
        }

        if (appForm.getRoute() == null || appForm.getRoute().isBlank()) {
            return "利用区間を入力してください。";
        }

        if (appForm.getAmount() == null || appForm.getAmount().compareTo(BigDecimal.ONE) <= 0) {
            return "金額は1以上で入力してください。";
        }

        if (appForm.getPurpose() == null || appForm.getPurpose().isBlank()) {
            return "利用目的を入力してください。";
        }

        // すべてOK
        return "OK";
    }
}*/
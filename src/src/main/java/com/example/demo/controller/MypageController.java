package com.example.demo.controller;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.common.CommonButtonHelper;
import com.example.demo.common.DisplayInfoHelper;
import com.example.demo.dto.ApplicationCardDTO;
import com.example.demo.form.ApplicationForm;
import com.example.demo.form.BusinessExpenseForm;
import com.example.demo.form.CommuteTransportForm;
import com.example.demo.form.MyForm;
import com.example.demo.form.SingleTransportForm;
import com.example.demo.model.T_HEADER;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.ExpenseIdService;
import com.example.demo.service.HeaderService;


@Controller
@SessionAttributes("expenseMonth")
public class MypageController {
	
	@Autowired
	private HeaderService headerService;
	
	@Autowired
	private ExpenseIdService expenseIdService;
	
	@Autowired
	private ApplicationService appService;
	
	@Autowired
	private DisplayInfoHelper diHelper;
	
	@Autowired
	private CommonButtonHelper cbHelper;
	
	//領収書画像を保存する サーバー上のディレクトリパス。
	//相対パス "uploads/" は、Spring Boot の起動ディレクトリ直下の uploads/ フォルダを意味する。
    private static final String UPLOAD_DIR = "uploads/";

	//★：マイページ画面を表示
	@GetMapping("/mypage")
	public String showMypage(Model model,HttpSession session,SessionStatus status) {
	    	    
	    //セッションに保存されている以下の値を切断する
		session.removeAttribute("expenseId");		//申請ID
		session.removeAttribute("headerInfo");		//ヘッダー情報
		session.removeAttribute("expenseMonth");	//申請タイトル情報
		session.removeAttribute("getST");			//単発交通費リスト
		session.removeAttribute("getCT");			//定期交通費リスト
		session.removeAttribute("getBE");			//業務経費リスト
		session.removeAttribute("newExpenseId");	//コピー後新申請ID
		session.removeAttribute("newExpenseMonth");	//コピー後新申請ID
		//session.invalidate();
		status.setComplete();
		
		String userId = (String)session.getAttribute("userId");
		
		//userIdを元に保存フラグ「０」の申請データをDB上から削除する
		List<String> expenseIds = appService.findExpenseIdsWithFlagZero(userId);
		int deleteBySaveFlagOfZero = appService.deleteBySaveFlagOfZero(expenseIds);
		
		if(userId != null) {
			
			//マイページの申請履歴欄の表示準備
			List<ApplicationCardDTO> findApplicationCards = appService.findApplicationCards(userId);
			session.setAttribute("findApplicationCards",findApplicationCards);
			model.addAttribute("findApplicationCards", findApplicationCards);

			// 重複を排除し、年でソートして昇順または降順にする
			List<Integer> findExpenseYear = appService.findExpenseYear(userId);
			List<Integer> uniqueYears = findExpenseYear.stream()
			    .distinct()
			    .sorted(Comparator.reverseOrder())  // 降順にして最新年を先頭に
			    .collect(Collectors.toList());

			Integer latestYear = uniqueYears.isEmpty() ? null : uniqueYears.get(0);

			session.setAttribute("uniqueYears", uniqueYears);
			session.setAttribute("latestYear", latestYear);
			model.addAttribute("latestYear", latestYear);
			
			return "mypage/mypage";
		
		}else{
		
			return "login/login";
		}
	}
	
	//★：表示年の更新
	@GetMapping("/mypage/filter")
	public String updateDisplayYear(@RequestParam("expenseYear") int expenseYear,Model model,
			                     HttpSession session,
			                     ApplicationForm appForm) {
		
		//ユーザーIDの取得
		String userId = (String)session.getAttribute("userId");
		
		//選択された年のマイページカードを取得する
		List<ApplicationCardDTO> findUpdateApplicationCards = appService.findUpdateApplicationCards(userId,expenseYear);
		session.setAttribute("findApplicationCards", findUpdateApplicationCards);
		
		model.addAttribute("latestYear", expenseYear);
		session.setAttribute("latestYear", expenseYear);
		
		return "mypage/mypage";
	}

	//★：確認画面を表示
	@GetMapping("/mypage/detail/{id}")
	public String showDetailPage(@PathVariable("id") int expenseId,Model model,
			                     HttpSession session,
			                     ApplicationForm appForm,
								 SingleTransportForm singleTransportForm,
								 CommuteTransportForm commuteTransportForm,
								 BusinessExpenseForm businessExpenseForm) {
				
		//★：確認ページへの遷移（URLが押下されたとき）
		if(expenseId != 0) {
			
			//モデルに各インスタンス追加
	        model.addAttribute("appForm", appForm);
			model.addAttribute("singleTrantportForm", singleTransportForm);
			model.addAttribute("commuteTransportForm", commuteTransportForm);
			model.addAttribute("businessExpenseForm", businessExpenseForm);
			model.addAttribute("roundTripFlag", 2);
			//model.addAttribute("roundTripFlag", "2"); // 初期値を設定
			
			//申請IDをセッションに追加
			session.setAttribute("expenseId", expenseId);
			
			//選択された申請IDのヘッダー情報を取得
			T_HEADER headerInfo = expenseIdService.getHeaderByExpenseId(expenseId);
			
			//セッションとmodelにヘッダー情報と申請タイトルを追加して、HTMLに渡す
			model.addAttribute("headerInfo",headerInfo);
			session.setAttribute("headerInfo",headerInfo);
			model.addAttribute("expenseMonth", headerInfo.getExpenseMonth());
			
			//申請内容と合算金額の取得
			diHelper.setupDisplayInfo(model, session, expenseId);
			
			//申請済かそうでないかチェック
			int checkAppStatus = appService.checkAppStatus(expenseId);
			model.addAttribute("checkAppStatus",checkAppStatus);
			
			//申請ボタン出現させていいかどうかチェック
			int selectApplicationButton = appService.selectApplicationButton(expenseId);
			model.addAttribute("selectApplicationButton",selectApplicationButton);
			
			return "mypage/details";
		}
		return "mypage/mypage";
	}
	
	//★：コピーして新規申請画面への遷移　or データ削除
	@PostMapping("/mypage/copyAndDelete")
	public String copySubmission(HttpSession session,
			                     @RequestParam("expenseId") int expenseId,
			                     @RequestParam("action") String action,
			                     Model model,ApplicationForm appForm) {
		
		// appFormをモデルに追加
        model.addAttribute("appForm", new MyForm());
		
		//★：コピーして新規申請画面へ遷移
		if("copy".equals(action)) {
			
			//選択された申請の申請IDをセッションに保存する
			session.setAttribute("expenseId", expenseId);
			
			//申請IDからヘッダー情報を取得して、セッションに保存する
			T_HEADER headerInfo = expenseIdService.getHeaderByExpenseId(expenseId);
			session.setAttribute("headerInfo",headerInfo);
			
		    //「コピーして新規申請」画面に遷移する
			return "redirect:/application/copy_application";
			
		//★：データ削除
		}else if("delete".equals(action)) {
			
			//選択された申請IDのヘッダー情報および明細情報を削除する
			int deleteByDetailIdOfExpenseId = appService.deleteByDetailIdOfExpenseId(expenseId);
			int deleteByExpenseIdOfExpenseId = appService.deleteByExpenseIdOfExpenseId(expenseId);
			
			if(deleteByDetailIdOfExpenseId >= 0 && deleteByExpenseIdOfExpenseId >= 1) {
				model.addAttribute("confirmMessage", "削除が完了しました");
			}else {
				model.addAttribute("confirmMessage", "削除が失敗しました");
			}
			
			//マイページの申請履歴欄の表示準備
			String userId = (String)session.getAttribute("userId");			
			List<ApplicationCardDTO> findApplicationCards = appService.findApplicationCards(userId);
			session.setAttribute("findApplicationCards",findApplicationCards);
			model.addAttribute("findApplicationCards", findApplicationCards);

			// 重複を排除し、年でソートして昇順または降順にする
			List<Integer> findExpenseYear = appService.findExpenseYear(userId);
			List<Integer> uniqueYears = findExpenseYear.stream()
			    .distinct()
			    .sorted(Comparator.reverseOrder())  // 降順にして最新年を先頭に
			    .collect(Collectors.toList());

			Integer latestYear = uniqueYears.isEmpty() ? null : uniqueYears.get(0);

			session.setAttribute("uniqueYears", uniqueYears);
			session.setAttribute("latestYear", latestYear);
			model.addAttribute("latestYear", latestYear);
			
			return "mypage/mypage";			
		}
		
	    //「コピーして新規申請」画面に遷移する
		return "/mypage/mypage";

	}
	
	
	//★：確認ページにてPOSTリクエストでフォームが送信された際の処理
	@PostMapping("/mypage")
	public String handleFormSubmission(HttpSession session,
									  @RequestParam("action") String action,
									  Model model,ApplicationForm appForm,
									  SingleTransportForm singleTransportForm,
									  CommuteTransportForm commuteTransportForm,
									  BusinessExpenseForm businessExpenseForm) throws IOException {
	
		model.addAttribute("appForm", appForm);
		model.addAttribute("singleTrantportForm", singleTransportForm);
		model.addAttribute("commuteTransportForm", commuteTransportForm);
		model.addAttribute("businessExpenseForm", businessExpenseForm);
		model.addAttribute("expenseMonth", appForm.getExpenseMonth());

		//★：「申請」ボタン押下
		if("apply".equals(action)) {
			 
			//申請IDの取得
			T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
			int expenseId = headerInfo.getExpenseId();

			cbHelper.applicationHelper(model, session, expenseId);
			
			return "mypage/details";
			
		//★：「削除」ボタン押下
		}else if("delete".equals(action)) {
			
			// チェックボックスから明細IDを取得
			List<Integer> detailIds = appForm.getCheckboxID();
			
			//申請IDの取得
			T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
			int expenseId = headerInfo.getExpenseId();
			model.addAttribute("headerInfo", headerInfo);
			
			cbHelper.deleteHelper(model, session, detailIds, expenseId);
			 
			return "mypage/details";
			
		//★：「保存」ボタン押下
		}else if("register".equals(action)) {

			//申請IDの取得
			T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
			int expenseId = headerInfo.getExpenseId();
			
			//申請の中に日付の入っていない明細がないかの確認
			int checkDate = appService.checkDate(expenseId);
			 
			//該当する値がなければ保存フラグを1にしたりする
			if(checkDate == 0) {
				cbHelper.saveHelper(model, session, expenseId);
			}else if(checkDate >= 1) {
				model.addAttribute("confirmMessage", "利用日付や定期開始日、終了日が入力されていないデータがあるので保存できません。");
				//申請内容と合算金額の取得
				diHelper.setupDisplayInfo(model, session, expenseId);
				 
				//申請ボタン出現させていいかどうかチェック
				int selectApplicationButton = appService.selectApplicationButton(expenseId);
				if(selectApplicationButton == 1) {
					model.addAttribute("selectApplicationButton",selectApplicationButton);
				}
				 
			}

			return "mypage/details";
		}
		return "mypage/mypage";
	}
	
	//★：単発交通費モーダルの「保存」ボタン押下
	@PostMapping("/mypage/save-single")
	public String saveSingle(@ModelAttribute @Valid SingleTransportForm singleTransportForm,
					         BindingResult stResult,
					         @ModelAttribute ApplicationForm appForm,Model model,
					         HttpSession session,SessionStatus status) throws IOException {
		
		model.addAttribute("appForm", appForm);
		model.addAttribute("commuteTransportForm", new CommuteTransportForm());
		model.addAttribute("businessExpenseForm",new BusinessExpenseForm());
		
		//保存されたヘッダー情報から申請IDを取得する
	    T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
	    int expenseId = headerInfo.getExpenseId();
		
		//モーダル画面で必須事項に未入力発生時(モーダルにエラーメッセージを送る）
		if (stResult.hasErrors()) {
			cbHelper.stSaveErrorHelper(singleTransportForm, appForm, model, session, expenseId);
		    return "mypage/details";
		}
		
		cbHelper.stSaveHelper(expenseId, singleTransportForm, model, session);
	    
	    // 同じ画面に戻る（モーダルはHTML内のまま）
	    return "mypage/details";
	}
	
	//★：定期交通費モーダルの「保存」ボタン押下
	@PostMapping("/mypage/save-commute")
	public String saveCommute(@ModelAttribute @Valid CommuteTransportForm commuteTransportForm,
					          BindingResult ctResult,
					          @ModelAttribute ApplicationForm appForm,Model model,
					          HttpSession session,SessionStatus status) throws IOException {
		
		model.addAttribute("appForm", new ApplicationForm());
		model.addAttribute("singleTransportForm", new SingleTransportForm());
		model.addAttribute("businessExpenseForm", new BusinessExpenseForm());
		
		//保存されたヘッダー情報から申請IDを取得する
	    T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
	    int expenseId = headerInfo.getExpenseId();
		
		//モーダル画面で必須事項に未入力発生時(モーダルにエラーメッセージを送る）
		if (ctResult.hasErrors()) {
			cbHelper.ctSaveErrorHelper(commuteTransportForm, appForm, model, session, expenseId);
			return "mypage/details";
		}
		
		cbHelper.ctSaveHelper(expenseId, commuteTransportForm, model, session);

	    // 同じ画面に戻る（モーダルはHTML内のまま）
	    return "mypage/details";
	}
	
	//★：業務経費モーダルの「保存」ボタン押下
	@PostMapping("/mypage/save-business")
	public String saveBusiness(@ModelAttribute @Valid BusinessExpenseForm businessExpenseForm,
					           BindingResult beResult,
					           @ModelAttribute ApplicationForm appForm, Model model,
					           HttpSession session,SessionStatus status) throws IOException {
		
		model.addAttribute("appForm", new ApplicationForm());
		model.addAttribute("singleTransportForm", new SingleTransportForm());
		model.addAttribute("commuteTransportForm", new CommuteTransportForm());
		
    	//保存されたヘッダー情報から申請IDを取得する
	    T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
	    int expenseId = headerInfo.getExpenseId();
	
		//モーダル画面で必須事項に未入力発生時(モーダルにエラーメッセージを送る）
		if (beResult.hasErrors()) {
			cbHelper.beSaveErrorHelper(businessExpenseForm, appForm, model, session, expenseId);
			return "mypage/details";
		}
		
		MultipartFile  file = appForm.getReceipt(); // OK
		
		cbHelper.beSaveHelper(expenseId, businessExpenseForm, model, session, file, UPLOAD_DIR);

	    // 同じ画面に戻る（モーダルはHTML内のまま）
	    return "mypage/details";
	}
}
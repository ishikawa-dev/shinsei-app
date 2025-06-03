package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.common.CommonButtonHelper;
import com.example.demo.common.DisplayInfoHelper;
import com.example.demo.form.ApplicationForm;
import com.example.demo.form.BusinessExpenseForm;
import com.example.demo.form.CommuteTransportForm;
import com.example.demo.form.SingleTransportForm;
import com.example.demo.model.T_HEADER;
import com.example.demo.service.ApplicationService;

@Controller
@SessionAttributes("expenseMonth")
public class ApplicationController {
	
	@Autowired
	private ApplicationService appService;
	
	@Autowired
	private DisplayInfoHelper diHelper;
	
	@Autowired
	private CommonButtonHelper cbHelper;
	
	@Autowired
	
	//領収書画像を保存する サーバー上のディレクトリパス。
	//相対パス "uploads/" は、Spring Boot の起動ディレクトリ直下の uploads/ フォルダを意味する。
    private static final String UPLOAD_DIR = "uploads/";
	
	@GetMapping("/application/new")
	public String showNewApplication(ApplicationForm appForm,Model model,
									 SingleTransportForm singleTransportForm,
									 CommuteTransportForm commuteTransportForm,
									 BusinessExpenseForm businessExpenseForm) {
		
		model.addAttribute("appForm", appForm); 
		model.addAttribute("singleTrantportForm", singleTransportForm);
		model.addAttribute("commuteTransportForm", commuteTransportForm);
		model.addAttribute("businessExpenseForm", businessExpenseForm);
		model.addAttribute("expenseMonth", appForm.getExpenseMonth());
		
		//new_application.htmlに遷移
		return "application/new_application";

	}
	
	//★：申請内容表示画面の各ボタンの処理
	@PostMapping("/application/saveTransport")
	public String saveTransport(@ModelAttribute ApplicationForm appForm,
								@RequestParam("action") String action ,
								Model model,HttpSession session,SessionStatus status)  throws IOException {
		
		model.addAttribute("singleTransportForm", new SingleTransportForm());
		model.addAttribute("commuteTransportForm", new CommuteTransportForm());
		model.addAttribute("businessExpenseForm", new BusinessExpenseForm());
		model.addAttribute("appForm",appForm);
		
		//★：申請タイトルの「確定」ボタン押下
		if("expenseMonthOK".equals(action)) {
			
			//ユーザーIDの取得
			String userId = (String)session.getAttribute("userId");
			
			//入力ヘッダー情報をエンティティに変換する
			T_HEADER headerContent = new T_HEADER();
			headerContent.setUserId(userId);
			headerContent.setExpenseMonth(appForm.getExpenseMonth());
			headerContent.setApprovalStatus(0);
			headerContent.setDisplayFlag(0);
			
			//現在時刻をエンティティに保存する
			LocalDate today = LocalDate.now();
			headerContent.setCreatedDate(today);
			
			//入力年を保存する
			headerContent.setExpenseYear(today.getYear());
		
			//入力されたヘッダー情報をmodelとsessionに保存(expenseMonthは＠SessionAttributeで連携済）			
			T_HEADER headerInfo = appService.saveHeader(headerContent);
			String expenseMonth = headerInfo.getExpenseMonth();
			model.addAttribute("expenseMonth", expenseMonth);
			session.setAttribute("headerInfo", headerInfo);
			model.addAttribute("headerInfo", headerInfo);
			
			// 同じ画面に戻る
		    return "application/new_application";
		 
		 }
	 
		 //★：「保存」ボタン押下
		 if("register".equals(action)) {
			 
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

			 return "application/new_application";
		 
	     //★：「申請」ボタン押下
		 }else if("apply".equals(action)) {
			 
			//申請IDの取得
			T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
			int expenseId = headerInfo.getExpenseId();
			
			cbHelper.applicationHelper(model, session,expenseId);

			return "application/new_application";
		 
		 //★：「削除」ボタン押下
		 }else if("delete".equals(action)){
			
			// チェックボックスから明細IDを取得
			List<Integer> detailIds = appForm.getCheckboxID();
			
			//申請IDの取得
			T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
			int expenseId = headerInfo.getExpenseId();
			model.addAttribute("headerInfo", headerInfo);
			
			cbHelper.deleteHelper(model, session, detailIds, expenseId);
			 
			return "application/new_application";
		 }
		 
		// 何かの条件が満たされない場合のエラーメッセージ
		model.addAttribute("message", "不正なアクションです。");
		return "application/new_application";
		
	}
	
	//★：単発交通費モーダルの「保存」ボタン押下
	@PostMapping("/application/save-single")
	public String saveSingle(@ModelAttribute @Valid SingleTransportForm singleTransportForm,
					   BindingResult stResult,
					   @ModelAttribute ApplicationForm appForm,Model model,
					   HttpSession session,SessionStatus status) throws IOException {
		
		model.addAttribute("appForm", appForm);
		model.addAttribute("commuteTransportForm", new CommuteTransportForm());
		model.addAttribute("businessExpenseForm",new BusinessExpenseForm());
		
	    //申請IDを取得する
		T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
		int expenseId = headerInfo.getExpenseId();
		
		//モーダル画面で必須事項に未入力発生時(モーダルにエラーメッセージを送る）
		if (stResult.hasErrors()) {
			cbHelper.stSaveErrorHelper(singleTransportForm, appForm, model, session, expenseId);
		    return "application/new_application";
		}
		
		cbHelper.stSaveHelper(expenseId, singleTransportForm, model, session);
	    
	    // 同じ画面に戻る（モーダルはHTML内のまま）
	    return "application/new_application";
	}
	
	//★：定期交通費モーダルの「保存」ボタン押下
	@PostMapping("/application/save-commute")
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
			return "application/new_application";
		}
	
		cbHelper.ctSaveHelper(expenseId, commuteTransportForm, model, session);
		
	    // 同じ画面に戻る（モーダルはHTML内のまま）
	    return "application/new_application";
	}
    
	//★：業務経費モーダルの「保存」ボタン押下
	@PostMapping("/application/save-business")
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
			return "application/new_application";
		}
		
		MultipartFile  file = businessExpenseForm.getReceipt(); // OK
		
		cbHelper.beSaveHelper(expenseId, businessExpenseForm, model, session, file, UPLOAD_DIR);
	    
	    // 同じ画面に戻る（モーダルはHTML内のまま）
	    return "application/new_application";
	}
}

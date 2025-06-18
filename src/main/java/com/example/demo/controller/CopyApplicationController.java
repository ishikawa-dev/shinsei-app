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
import com.example.demo.form.MyForm;
import com.example.demo.form.SingleTransportForm;
import com.example.demo.model.T_DETAILS;
import com.example.demo.model.T_HEADER;
import com.example.demo.service.ApplicationService;

@Controller
@SessionAttributes("newExpenseMonth")
public class CopyApplicationController {
	
	@Autowired
	private ApplicationService appService;
	
	@Autowired
	private DisplayInfoHelper diHelper;
	
	@Autowired
	private CommonButtonHelper cbHelper;
	
	//領収書画像を保存する サーバー上のディレクトリパス。
	//相対パス "uploads/" は、Spring Boot の起動ディレクトリ直下の uploads/ フォルダを意味する。
    private static final String UPLOAD_DIR = "uploads/";
	
	@GetMapping("/application/copy_application")
	public String showCopyApplication(ApplicationForm appForm,Model model,
									 SingleTransportForm singleTransportForm,
									 CommuteTransportForm commuteTransportForm,
									 BusinessExpenseForm businessExpenseForm) {
		
		model.addAttribute("appForm", appForm); 
		model.addAttribute("singleTrantportForm", singleTransportForm);
		model.addAttribute("commuteTransportForm", commuteTransportForm);
		model.addAttribute("businessExpenseForm", businessExpenseForm);
		model.addAttribute("expenseMonth", appForm.getExpenseMonth());
		
		//copy_application.htmlに遷移
		return "application/copy_application";

	}
	
	@PostMapping("/application/copy")
	public String copy(HttpSession session,MyForm form,
					   @RequestParam("action") String action,
					   Model model,ApplicationForm appForm) throws IOException {
	
		model.addAttribute("appForm", new ApplicationForm());
		model.addAttribute("singleTransportForm", new SingleTransportForm());
		model.addAttribute("commuteTransportForm", new CommuteTransportForm());
		model.addAttribute("businessExpenseForm", new BusinessExpenseForm());
		model.addAttribute("appForm",appForm);
		
		 //★：申請タイトルの「確定」ボタン押下
		 if("newExpenseMonthOK".equals(action)) {
			
			//ユーザーIDの取得
			String userId = (String)session.getAttribute("userId");
			
			//入力ヘッダー情報をエンティティに変換する
			T_HEADER headerContent = new T_HEADER();
			headerContent.setUserId(userId);
			headerContent.setExpenseMonth(appForm.getNewExpenseMonth());
			headerContent.setApprovalStatus(0);
			headerContent.setDisplayFlag(0);
			
			//現在時刻をエンティティに保存する
			LocalDate today = LocalDate.now();
			headerContent.setCreatedDate(today);
			
			//入力年を保存する
			headerContent.setExpenseYear(today.getYear());
			
			//入力されたヘッダー情報をmodelとsessionに保存(newExpenseMonthは＠SessionAttributeで連携済）			
			T_HEADER headerInfo = appService.saveHeader(headerContent);
			model.addAttribute("headerInfo", headerInfo);
			String newExpenseMonth = headerInfo.getExpenseMonth();
			model.addAttribute("newExpenseMonth", newExpenseMonth);
			session.setAttribute("headerInfo", headerInfo);
			
			//選択した申請内容取得
			int expenseId = (Integer)session.getAttribute("expenseId");
			diHelper.setupDisplayInfo(model, session, expenseId);
			
			//新しい申請IDを取得する
			int newExpenseId = headerInfo.getExpenseId();
			
			//コピーしたデータのうち必要項目だけ取り出し、新しい申請IDでDB登録
			setCopyData(newExpenseId, session);
			
			//新しい申請IDのヘッダー情報、合算金額の取得
			cbHelper.commonHelper(newExpenseId, model, session);
			
			// 同じ画面に戻る
		    return "application/copy_application";
		 
		 }
		
		//★：「申請」ボタン押下
		if("apply".equals(action)) {
			
			//申請IDを取得
			T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
			int expenseId = headerInfo.getExpenseId();
			 
			cbHelper.applicationHelper(model, session, expenseId);
			
			return "application/copy_application";
			
		//★：「削除」ボタン押下
		}else if("delete".equals(action)) {
		
			//チェックボックスから明細IDを取得
			List<Integer> detailIds = appForm.getCheckboxID();
			
			//申請IDの取得
			T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
			int expenseId =headerInfo.getExpenseId();
			model.addAttribute("headerInfo", headerInfo);
			
			cbHelper.deleteHelper(model, session, detailIds, expenseId);

			return "application/copy_application";
			
		//★：「保存」ボタン押下
		}else if("register".equals(action)) {
		
			//申請IDの取得
			T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
			int expenseId =headerInfo.getExpenseId();
			
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
			
			return "application/copy_application";
		}

		return "application/copy_application";
	
	}
	
	//★：単発交通費モーダルの「保存」ボタン押下
	@PostMapping("/application/copy/save-single")
	public String saveSingle(@ModelAttribute @Valid SingleTransportForm singleTransportForm,
					   BindingResult stResult,
					   @ModelAttribute ApplicationForm appForm,Model model,
					   HttpSession session,SessionStatus status) throws IOException {
		
		model.addAttribute("appForm", appForm);
		model.addAttribute("commuteTransportForm", new CommuteTransportForm());
		model.addAttribute("businessExpenseForm",new BusinessExpenseForm());
		
    	//保存されたヘッダー情報から申請IDを取得する
		T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
		int expenseId =headerInfo.getExpenseId();
		
		//モーダル画面で必須事項に未入力発生時(モーダルにエラーメッセージを送る）
		if (stResult.hasErrors()) {
			cbHelper.stSaveErrorHelper(singleTransportForm, appForm, model, session, expenseId);
			return "application/copy_application";
		}

		//appFormの単発交通費情報をエンティティに変換する
	    T_DETAILS singleTransportContent = new T_DETAILS();
	    singleTransportContent.setExpenseId(expenseId);
	    singleTransportContent.setUsageDate(appForm.getUsageDate());
	    singleTransportContent.setTransportationFacilities(appForm.getTransportationFacilities());
	    singleTransportContent.setRoute(appForm.getRoute());
	    singleTransportContent.setRoundTripFlag(appForm.getRoundTripFlag());
	    singleTransportContent.setAmount(appForm.getAmount());
	    singleTransportContent.setPurpose(appForm.getPurpose());
	    singleTransportContent.setExpenseType(1);		//経費種別→単発交通費
	    singleTransportContent.setSaveFlag(0);			//保存フラグ→未保存
	    
	    //明細IDがすでに生成されている場合
	    if(appForm.getDetailId() != null && appForm.getDetailId() != 0) {
	    	//明細IDをエンティティに保存する
	    	singleTransportContent.setDetailId(appForm.getDetailId());
	    }
	    
	    // 入力された情報をセッションとmodelに保存
	    if (singleTransportContent != null) {
	    	T_DETAILS savedSingleTransport = appService.saveApplication(singleTransportContent);
			if(savedSingleTransport != null) {
				 model.addAttribute("confirmMessage", "追加が完了しました");
			}
	    }
	    
		//入力日の更新
		int updateCreatedDate = appService.updateCreatedDate(expenseId);
		
		//ヘッダー情報、申請内容、合算金額の取得
		cbHelper.commonHelper(expenseId, model, session);
	    
	    // 同じ画面に戻る（モーダルはHTML内のまま）
	    return "application/copy_application";
	}
	
	//★：定期交通費モーダルの「保存」ボタン押下
	@PostMapping("/application/copy/save-commute")
	public String saveCommute(@ModelAttribute @Valid CommuteTransportForm commuteTransportForm,
					   BindingResult ctResult,
					   @ModelAttribute ApplicationForm appForm,Model model,
					   HttpSession session,SessionStatus status) throws IOException {
		
		model.addAttribute("appForm", new ApplicationForm());
		model.addAttribute("singleTransportForm", new SingleTransportForm());
		model.addAttribute("businessExpenseForm", new BusinessExpenseForm());
		
    	//申請IDを取得する
		T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
		int expenseId =headerInfo.getExpenseId();
		
		//モーダル画面で必須事項に未入力発生時(モーダルにエラーメッセージを送る）
		if (ctResult.hasErrors()) {
			cbHelper.ctSaveErrorHelper(commuteTransportForm, appForm, model, session, expenseId);
			return "application/new_application";
		}

		//appFormの単発交通費情報をエンティティに変換する
	    T_DETAILS commuteTransportContent = new T_DETAILS();
	    commuteTransportContent.setExpenseId(expenseId);
	    commuteTransportContent.setValidFrom(appForm.getValidFrom());
	    commuteTransportContent.setValidUntil(appForm.getValidUntil());
	    commuteTransportContent.setTransportationFacilities(appForm.getTransportationFacilities());
	    commuteTransportContent.setRoute(appForm.getRoute());
	    commuteTransportContent.setAmount(appForm.getAmount());
	    commuteTransportContent.setPurpose(appForm.getPurpose());
	    commuteTransportContent.setExpenseType(2);		//経費種別→定期交通費
	    commuteTransportContent.setSaveFlag(0);			//保存フラグ→未保存
	    
	    //明細IDがすでに生成されている場合
	    if(appForm.getDetailId() != null) {
	    	//明細IDをエンティティに保存する
	    	commuteTransportContent.setDetailId(appForm.getDetailId());
	    }
	    
		// 入力された情報をセッションとmodelに保存
	    if (commuteTransportContent != null) {
	    	T_DETAILS savedCommuteTransport = appService.saveApplication(commuteTransportContent);
			if(savedCommuteTransport != null) {
				 model.addAttribute("confirmMessage", "追加が完了しました");
			}
	    }
	    
		//入力日の更新
		int updateCreatedDate = appService.updateCreatedDate(expenseId);
		
		//ヘッダー情報、申請内容、合算金額の取得
		cbHelper.commonHelper(expenseId, model, session);

	    // 同じ画面に戻る（モーダルはHTML内のまま）
	    return "application/copy_application";
	}
	
	//★：業務経費モーダルの「保存」ボタン押下
	@PostMapping("/application/copy/save-business")
	public String saveBusiness(@ModelAttribute @Valid BusinessExpenseForm businessExpenseForm,
					   BindingResult beResult,
					   @ModelAttribute ApplicationForm appForm, Model model,
					   HttpSession session,SessionStatus status) throws IOException {
		
		model.addAttribute("appForm", new ApplicationForm());
		model.addAttribute("singleTransportForm", new SingleTransportForm());
		model.addAttribute("commuteTransportForm", new CommuteTransportForm());
		
    	//申請IDを取得する
		T_HEADER headerInfo = (T_HEADER)session.getAttribute("headerInfo");
		int expenseId =headerInfo.getExpenseId();
	
		//モーダル画面で必須事項に未入力発生時(モーダルにエラーメッセージを送る）
		if (beResult.hasErrors()) {
			cbHelper.beSaveErrorHelper(businessExpenseForm, appForm, model, session, expenseId);
			return "application/new_application";
		}

	    T_DETAILS businessExpenseContent = new T_DETAILS();
		
		MultipartFile  file = businessExpenseForm.getReceipt(); // OK
		
		//領収書のアップデートがある場合
		if (!file.isEmpty()) {
		    // MultipartFileのバイト配列を取得してエンティティにセットする
		    byte[] receiptBytes = file.getBytes();
		    businessExpenseContent.setReceipt(receiptBytes);
		}
	    
		//appFormの単発交通費情報をエンティティに変換する
		businessExpenseContent.setExpenseId(expenseId);
		businessExpenseContent.setUsageDate(businessExpenseForm.getUsageDate());
		businessExpenseContent.setOtherExpenseItem(businessExpenseForm.getOtherExpenseItem());
		businessExpenseContent.setAmount(businessExpenseForm.getAmount());
	    businessExpenseContent.setPurpose(businessExpenseForm.getPurpose());
	    businessExpenseContent.setExpenseType(3);		//経費種別→業務経費
	    businessExpenseContent.setSaveFlag(0);			//保存フラグ→未保存
	    
	    //明細IDがすでに生成されている場合
	    if(appForm.getDetailId() != null) {
	    	//明細IDをエンティティに保存する
	    	businessExpenseContent.setDetailId(appForm.getDetailId());
	    }
		
	    // 入力された情報をセッションとmodelに保存
	    if (businessExpenseContent != null) {
	    	T_DETAILS savedBuisinessExpense = appService.saveApplication(businessExpenseContent);
			if(savedBuisinessExpense != null) {
				 model.addAttribute("confirmMessage", "追加が完了しました");
			}
	    }
	    
		//入力日の更新
		int updateCreatedDate = appService.updateCreatedDate(expenseId);
		
		//ヘッダー情報、申請内容、合算金額の取得
		cbHelper.commonHelper(expenseId, model, session);
		
	    // 同じ画面に戻る（モーダルはHTML内のまま）
	    return "application/copy_application";
	}
	
	// ========================================================
	//各種経費のコピーデータのうち、DB登録必要なデータのみをリストに格納する
	// ========================================================
	public void setCopyData(int newExpenseId,HttpSession session) {
		
		//単発交通費
		List<T_DETAILS> getST = (List<T_DETAILS>)session.getAttribute("getSTDetails");
		if(getST != null) {
			for(T_DETAILS x:getST) {
				T_DETAILS newST = new T_DETAILS();
			    newST.setExpenseId(newExpenseId);
				//newST.setUsageDate(x.getUsageDate());
				newST.setTransportationFacilities(x.getTransportationFacilities());
				newST.setRoute(x.getRoute());
				newST.setRoundTripFlag(x.getRoundTripFlag());
				newST.setAmount(x.getAmount());
				newST.setPurpose(x.getPurpose());
				newST.setExpenseType(1);		//経費種別→単発交通費
				newST.setSaveFlag(0);	
				//DBへ追加
				appService.saveApplication(newST);
			}
		}
		
		//定期交通費
		List<T_DETAILS> getCT = (List<T_DETAILS>)session.getAttribute("getCTDetails");
		if(getCT != null) {
			for(T_DETAILS x:getCT) {
				T_DETAILS newCT = new T_DETAILS();
				newCT.setExpenseId(newExpenseId);
				//newCT.setValidFrom(x.getValidFrom());
				//newCT.setValidUntil(x.getValidUntil());
				newCT.setTransportationFacilities(x.getTransportationFacilities());
				newCT.setRoute(x.getRoute());
				newCT.setAmount(x.getAmount());
				newCT.setPurpose(x.getPurpose());
				newCT.setExpenseType(2);		//経費種別→単発交通費
				newCT.setSaveFlag(0);
				//DBへ追加
				appService.saveApplication(newCT);
			}
		}
		
		//業務経費
		List<T_DETAILS> getBE = (List<T_DETAILS>)session.getAttribute("getBEDetails");
		if(getBE != null) {
			for(T_DETAILS x:getBE) {
				T_DETAILS newBE = new T_DETAILS();
				newBE.setExpenseId(newExpenseId);
				//newBE.setUsageDate(x.getUsageDate());
				newBE.setOtherExpenseItem(x.getOtherExpenseItem());
				newBE.setAmount(x.getAmount());
				newBE.setPurpose(x.getPurpose());
				newBE.setExpenseType(3);		//経費種別→単発交通費
				newBE.setSaveFlag(0);	
				//DBへ追加
				appService.saveApplication(newBE);
			}
		}
	}

}

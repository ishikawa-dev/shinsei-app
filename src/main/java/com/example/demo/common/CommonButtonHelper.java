package com.example.demo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.form.ApplicationForm;
import com.example.demo.form.BusinessExpenseForm;
import com.example.demo.form.CommuteTransportForm;
import com.example.demo.form.SingleTransportForm;
import com.example.demo.model.T_DETAILS;
import com.example.demo.model.T_HEADER;
import com.example.demo.service.ApplicationService;

@Component
public class CommonButtonHelper {

	@Autowired
	private ApplicationService appService;
	
	@Autowired
	private DisplayInfoHelper diHelper;
	
	//★：「保存」ボタン押下用
	public void saveHelper(Model model,HttpSession session,int expenseId) {
		
		 //入力されている申請の保存フラグを「1：(保存済)」に更新する
		 int updateOfSaveFlag = appService.updateOfSaveFlag(expenseId);
		 
		 if(updateOfSaveFlag != 0) {
			 model.addAttribute("confirmMessage", "保存が完了しました");
		 }
		 
		//申請内容と合算金額の取得
		diHelper.setupDisplayInfo(model, session, expenseId);
		 
		//申請ボタン出現させていいかどうかチェック
		 int selectApplicationButton = appService.selectApplicationButton(expenseId);
		 if(selectApplicationButton == 1) {
			 model.addAttribute("selectApplicationButton",selectApplicationButton);
		 }
		 
		//ヘッダー情報の更新
		T_HEADER updateHeaderInfo = appService.findHeaderByExpenseId(expenseId);
		session.setAttribute("headerInfo", updateHeaderInfo);
		model.addAttribute("headerInfo", updateHeaderInfo);
	}
	
	//★：「削除」ボタン押下用
	public void deleteHelper(Model model,HttpSession session,List<Integer> detailIds,int expenseId) {
		
		//DBから削除
		if(detailIds != null) {
			 int deleteDetailCount = appService.deleteByDetailIds(detailIds);
			 if (deleteDetailCount >= 1) {
			     model.addAttribute("confirmMessage", "削除が完了しました（DB）");
			 } 
		 }else{
		     model.addAttribute("confirmMessage", "削除に失敗しました（明細が見つかりません）");
		 }

		//申請内容と合算金額の取得
		diHelper.setupDisplayInfo(model, session, expenseId);
	}
	
	//★：「申請」ボタン押下用
	public void applicationHelper(Model model,HttpSession session,int expenseId) {
		
		 //T_HEADERのexpense_flagを「1」にUPDATE
		int updateExpenseId = appService.updateExpenseFlag(expenseId);
		
		//T_HEADERの申請日をシステム日付にUPDATE
		int updateSubmissionDate = appService.updateSubmissionDate(expenseId);
		
		//T_HEADERのステータスを「3 = 申請中」にUPDATE
		int updateStutasOfTHREE = appService.updateStutasOfTHREE(expenseId);
		
		if(updateExpenseId >= 1 && updateSubmissionDate >= 1 && updateStutasOfTHREE >= 1) {
			model.addAttribute("confirmMessage", "申請が完了しました");
		}else {
			model.addAttribute("confirmMessage", "申請が失敗しました");
		}
		
		//ヘッダー情報の更新
		T_HEADER updateHeaderInfo = appService.findHeaderByExpenseId(expenseId);
		session.setAttribute("headerInfo", updateHeaderInfo);
		model.addAttribute("headerInfo", updateHeaderInfo);
	    
		//申請内容と合算金額の取得
		diHelper.setupDisplayInfo(model, session, expenseId);
	    
		//申請済かそうでないかチェック
		int checkAppStatus = appService.checkAppStatus(expenseId);
		model.addAttribute("checkAppStatus",checkAppStatus);
		
		//申請ボタン出現させていいかどうかチェック
		int selectApplicationButton = appService.selectApplicationButton(expenseId);
		model.addAttribute("selectApplicationButton",selectApplicationButton);
	}
	
	//★★★：共通処理
	public void commonHelper(int expenseId,Model model,
				  			 HttpSession session) {
		
		//ヘッダー情報の更新
		T_HEADER updateHeaderInfo = appService.findHeaderByExpenseId(expenseId);
		session.setAttribute("headerInfo", updateHeaderInfo);
		model.addAttribute("headerInfo", updateHeaderInfo);
		model.addAttribute("expenseMonth",updateHeaderInfo.getExpenseMonth());
	    
		//申請内容と合算金額の取得
		diHelper.setupDisplayInfo(model, session, expenseId);
		
	}
	
	//★：単発交通費「保存」ボタン押下
	//①エラー発生時用
	public void stSaveErrorHelper(SingleTransportForm singleTransportForm,
			   					  ApplicationForm appForm,Model model,
			   					  HttpSession session,int expenseId) {
		
		model.addAttribute("showStModal", true);
		model.addAttribute("singleTransportForm", singleTransportForm);
		// 他のフォームもセット
		model.addAttribute("appForm", appForm);
		model.addAttribute("ctForm", new CommuteTransportForm());
		model.addAttribute("beForm", new BusinessExpenseForm());
		
		commonHelper(expenseId,model,session);  
	}
	
	//②エラー発生なし用
	public void stSaveHelper(int expenseId,SingleTransportForm singleTransportForm,
							 Model model,HttpSession session) {
		
		//appFormの単発交通費情報をエンティティに変換する
	    T_DETAILS singleTransportContent = new T_DETAILS();
	    singleTransportContent.setExpenseId(expenseId);
	    singleTransportContent.setUsageDate(singleTransportForm.getUsageDate());
	    singleTransportContent.setTransportationFacilities(singleTransportForm.getTransportationFacilities());
	    singleTransportContent.setRoute(singleTransportForm.getRoute());
	    singleTransportContent.setRoundTripFlag(singleTransportForm.getRoundTripFlag());
	    singleTransportContent.setAmount(singleTransportForm.getAmount());
	    singleTransportContent.setPurpose(singleTransportForm.getPurpose());
	    singleTransportContent.setExpenseType(1);				//経費種別→単発交通費
	    singleTransportContent.setSaveFlag(0);					//保存フラグ→未保存
		
	    //入力された情報をDBに保存
		if (singleTransportContent != null) {
			T_DETAILS savedSingleTransport = appService.saveApplication(singleTransportContent);
			if(savedSingleTransport != null) {
				 model.addAttribute("confirmMessage", "追加が完了しました");
			}
		}	
		
		//入力日の更新
		int updateCreatedDate = appService.updateCreatedDate(expenseId);
		
		//ヘッダー情報、申請内容、合算金額の取得
		commonHelper(expenseId, model, session);
	    
	}
	
	//★：定期交通費「保存」ボタン押下
	//①エラー発生時用
	public void ctSaveErrorHelper(CommuteTransportForm commuteTransportForm,
				  				  ApplicationForm appForm,Model model,
				  				  HttpSession session,int expenseId) {

		model.addAttribute("showCtModal", true);
		model.addAttribute("commuteTransportForm", commuteTransportForm);
		// 他もセット
		model.addAttribute("appForm", appForm);
		model.addAttribute("singleTransportForm", new SingleTransportForm());
		model.addAttribute("businessExpenseForm", new BusinessExpenseForm());
		
		commonHelper(expenseId,model,session);
	}
	
	//②エラー発生なし用
	public void ctSaveHelper(int expenseId,CommuteTransportForm commuteTransportForm,
							 Model model,HttpSession session) {
		
		//appFormの定期交通費情報をエンティティに変換する
	    T_DETAILS commuteTransportContent = new T_DETAILS();
	    commuteTransportContent.setExpenseId(expenseId);
	    commuteTransportContent.setValidFrom(commuteTransportForm.getValidFrom());
	    commuteTransportContent.setValidUntil(commuteTransportForm.getValidUntil());
	    commuteTransportContent.setTransportationFacilities(commuteTransportForm.getTransportationFacilities());
	    commuteTransportContent.setRoute(commuteTransportForm.getRoute());
	    commuteTransportContent.setAmount(commuteTransportForm.getAmount());
	    commuteTransportContent.setPurpose(commuteTransportForm.getPurpose());
	    commuteTransportContent.setExpenseType(2);					//経費種別→定期交通費
	    commuteTransportContent.setSaveFlag(0);						//保存フラグ→未保存
		
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
		commonHelper(expenseId, model, session);
	}
	
	//★：業務経費「保存」ボタン押下用
	//①エラー発生時用
	public void beSaveErrorHelper(BusinessExpenseForm businessExpenseForm,
							 ApplicationForm appForm,Model model,
							 HttpSession session,int expenseId) {
		
		model.addAttribute("showBeModal", true);
		model.addAttribute("businessExpenseForm", businessExpenseForm);
		model.addAttribute("appForm", appForm);
		model.addAttribute("singleTransportForm", new SingleTransportForm());
		model.addAttribute("commuteTransportForm", new CommuteTransportForm());
		
		commonHelper(expenseId,model,session);
	}
	
	//②エラー発生なし用
	public void beSaveHelper(int expenseId,BusinessExpenseForm businessExpenseForm,
							 Model model,HttpSession session,MultipartFile file,String UPLOAD_DIR) throws IOException{
		
		T_DETAILS businessExpenseContent = new T_DETAILS();

		//領収書のアップデートがある場合
		if (!file.isEmpty()) {
            // uploadフォルダが存在しない場合は作成
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();
            
            // ファイル名をユニークにする（例：タイムスタンプ付き）
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // 保存先のパス（Paths.get() で保存先のフルパスを作成。）
            Path path = Paths.get(UPLOAD_DIR + fileName);
            String filePath = path.toString();

            // ファイル保存、すでに同じ名前のファイルがある場合は、上書き（REPLACE_EXISTING）
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            
            businessExpenseContent.setReceipt(filePath);
        }
	    
		//appFormの業務経費情報をエンティティに変換する
	    businessExpenseContent.setExpenseId(expenseId);
	    businessExpenseContent.setUsageDate(businessExpenseForm.getUsageDate());
	    businessExpenseContent.setOtherExpenseItem(businessExpenseForm.getOtherExpenseItem());
	    businessExpenseContent.setAmount(businessExpenseForm.getAmount());
	    businessExpenseContent.setPurpose(businessExpenseForm.getPurpose());
	    businessExpenseContent.setExpenseType(3);				//経費種別→業務経費
	    businessExpenseContent.setSaveFlag(0);					//保存フラグ→未保存
		
	    // 入力された情報をセッションとmodelに保存
	    if (businessExpenseContent != null) {
	    	T_DETAILS savedBusinessExpense = appService.saveApplication(businessExpenseContent);
			if(savedBusinessExpense != null) {
				 model.addAttribute("confirmMessage", "追加が完了しました");
			}
	    }
	    
		//入力日の更新
		int updateCreatedDate = appService.updateCreatedDate(expenseId);
	    
		//ヘッダー情報、申請内容、合算金額の取得
		commonHelper(expenseId, model, session);
	}
}
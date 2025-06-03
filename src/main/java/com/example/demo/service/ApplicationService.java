package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ApplicationCardDTO;
import com.example.demo.dto.HeaderUserDTO;
import com.example.demo.model.T_DETAILS;
import com.example.demo.model.T_HEADER;
import com.example.demo.repository.TDetailsRepository;
import com.example.demo.repository.THeaderRepository;

@Service
public class ApplicationService {
	
	@Autowired
	private THeaderRepository thRepository;
	
	@Autowired
	private TDetailsRepository tdRepository;
	
	//★★～T_HEADER～★★
	
	//▶▶SELECT
	// ========================================================
	// 申請済かそうでないかの確認
	// ========================================================
	public int checkAppStatus(int expenseId) {
		int checkAppStatus = thRepository.checkAppStatus(expenseId);
		return checkAppStatus;
	}
	
	// ========================================================
	// 申請IDを検索値としてヘッダー情報を取得する
	// ========================================================
    public T_HEADER findHeaderByExpenseId(int expenseId){
    	T_HEADER findHeaderByExpenseId = thRepository.findHeaderByExpenseId(expenseId);
    	return findHeaderByExpenseId;
    }
    
	// ========================================================
	// 承認ステータス=3のもののヘッダー情報を取得する
	// ========================================================
    public List<HeaderUserDTO> findApprovalStatusTHREE(){
    	List<HeaderUserDTO> findApprovalStatusTHREE = thRepository.findApprovalStatusTHREE();
    	return findApprovalStatusTHREE;
    }
    
	// ========================================================
	// 検索結果でかえってきた申請IDを検索値としてユーザー名、申請タイトル、申請日を取得する
	// ========================================================
    public List<HeaderUserDTO> findSearchResult(List<Integer> expenseIdList){
    	List<HeaderUserDTO> findSearchResult = thRepository.findSearchResult(expenseIdList);
    	return findSearchResult;
    }
    
	// ========================================================
	// 申請IDを検索値として承認ステータスが2＝却下、3＝承認済かどうかチェックする
	// ========================================================
    public int checkApprovalStatus(int expenseId) {
    	int checkApprovalStatus = thRepository.checkApprovalStatus(expenseId);
    	return checkApprovalStatus;
    }
    
	// ========================================================
	// ユーザーIDを検索値としてマイページの申請カードの中身を取得する
	// ========================================================
    public List<ApplicationCardDTO> findApplicationCards(String userId){
    	List<ApplicationCardDTO> findApplicationCards = thRepository.findApplicationCards(userId);
    	return findApplicationCards;
    }
    
	// ========================================================
	// ユーザーIDを検索値として申請フラグが「０」の申請IDの一覧を取得する
	// ========================================================
    public List<Integer> findExpenseYear(String userId){
    	List<Integer> findExpenseYear = thRepository.findExpenseYear(userId);
    	return findExpenseYear;
    }
    
	// ========================================================
	// ユーザーIDと選択年を検索値としてマイページの申請カードの中身を取得する
	// ========================================================
    public List<ApplicationCardDTO> findUpdateApplicationCards(String userId,int expenseYear){
    	List<ApplicationCardDTO> findUpdateApplicationCards = thRepository.findUpdateApplicationCards(userId,expenseYear);
    	return findUpdateApplicationCards;
    }
   
		
	//▶▶INSERT
	// ========================================================
	//ヘッダー情報を保存するメソッド
	// ========================================================
	public T_HEADER saveHeader(T_HEADER headerContent) {
		T_HEADER savedHeader = thRepository.save(headerContent);
		return savedHeader;
	}
	
	//▶▶UPDATE
	// ========================================================
	// 「申請」ボタンが押された申請IDの申請フラグを「1 = 申請済」に更新する
	// ========================================================
	public int updateExpenseFlag(int expenseId) {
	    // updateExpenseFlag メソッドが返すのは更新された行数
	    return thRepository.updateExpenseFlag(expenseId);
	}
	
	// ========================================================
	// 「申請」ボタンが押された申請のステータスを「3 = 申請中」に更新する
	// ========================================================
	 public int updateStutasOfTHREE(int expenseId) {
		 int updateStutasOfTHREE = thRepository.updateStutasOfTHREE(expenseId);
		 return updateStutasOfTHREE;
	 }
	
	// ========================================================
	// 「申請」ボタンが押された申請IDの申請日をシステム日付に更新する
	// ========================================================
	 public int updateSubmissionDate(int expenseId) {
		int updateSubmissionDate = thRepository.updateSubmissionDate(expenseId);
		return updateSubmissionDate;
	 }
	 
	// ========================================================
	// 該当する申請IDの入力日をシステム日付に更新する
	// ========================================================
	 public int updateCreatedDate(int expenseId) {
		 int updateCreatedDate = thRepository.updateCreatedDate(expenseId);
		 return updateCreatedDate;
	 }
		
	 // ========================================================
	// 「承認」ボタンが押された申請のステータスを「2 = 承認済」に更新する
	// ========================================================
	 public int updateStutasOfTWO(int expenseId) {
		 int updateStutasOfTWO = thRepository.updateStutasOfTWO(expenseId);
		 return updateStutasOfTWO;
	 }
	 
	// ========================================================
	// 「却下」ボタンが押された申請のステータスを「1 = 却下済」に更新する
	// ========================================================
	 public int updateStutasOfONE(int expenseId) {
		 int updateStutasOfONE = thRepository.updateStutasOfONE(expenseId);
		 return updateStutasOfONE;
	 }
 
	// ========================================================
	// 「承認」ボタンが押された申請の支払い日を更新する
	// ========================================================
	 public int updateSubmissionDate(LocalDate submissionDate,int expenseId) {
		 int updatePaymentDate = thRepository.updatePaymentDate(submissionDate,expenseId);
		 return updatePaymentDate;
	 }

	// ========================================================
	// 「却下」ボタンが押された申請の却下理由を更新する
	// ========================================================
	 public int updateDismissedReason(String dismissedReason,int expenseId) {
		 int updateDismissedReason = thRepository.updateDismissedReason(dismissedReason,expenseId);
		 return updateDismissedReason;
	 }
	 
	// ========================================================
	// 更新一覧から表示された申請の表示フラグを「1 = 表示済」に更新する
	// ========================================================
	 public int updateDisplayFlag(int expenseId) {
	 	int updateDisplayFlag = thRepository.updateDisplayFlag(expenseId);
	 	return updateDisplayFlag;
	 }

	//▶▶DELETE
	// ========================================================
	// ユーザーIDを検索値として保存フラグ「0 = 未保存」のデータを削除し、削除できたデータ数を返す
	// ========================================================
	public List<String> findExpenseIdsWithFlagZero(String userId) {
		List<String> findExpenseIdsWithFlagZero = thRepository.findExpenseIdsWithFlagZero(userId);
		return findExpenseIdsWithFlagZero;
	}
	
	// ========================================================
	// 申請IDを検索値として紐づく明細情報を全削除する
	// ========================================================
	public int deleteByExpenseIdOfExpenseId(int expenseId) {
		int deleteByExpenseIdOfExpenseId = thRepository.deleteByExpenseIdOfExpenseId(expenseId);
		return deleteByExpenseIdOfExpenseId;
	}


	//★★～T_DETAILS～★★
	
	//▶▶SELECT
	// ========================================================
	//データベースから申請IDと経費種別を検索値としてそれに紐づく情報を取得するメソッド
	// ========================================================
	public List<T_DETAILS> getDBDisplayInfo(int expenseId,int expenseType) {
		
		List<T_DETAILS> getDetails = null;

		if (expenseType == 1) {
			// 単発交通費の場合
			getDetails = tdRepository.findSavedDetails(expenseId, 1);
		} else if (expenseType == 2) {
			// 定期交通費の場合
			getDetails = tdRepository.findSavedDetails(expenseId, 2);
		} else if (expenseType == 3) {
			// 業務経費の場合
			getDetails = tdRepository.findSavedDetails(expenseId, 3);
		} else {
			// 不正な種別
			throw new IllegalArgumentException("無効な経費種別です: " + expenseType);
		}

		return getDetails;
	}
	
	// ========================================================
	// 申請IDを検索値として各経費種別の合算金額の値を取得する
	// ========================================================
	public int totalAmountInfo(int expenseId){
		List<BigDecimal> totalAmountInfo = tdRepository.totalAmountInfo(expenseId);
		BigDecimal totalAmountBD = BigDecimal.ZERO;;
		for(BigDecimal x : totalAmountInfo) {
			totalAmountBD = totalAmountBD.add(x);
		}
		int totalAmount = totalAmountBD.intValue();
		return totalAmount;
	}
	
	// ========================================================
	// 申請IDと保存フラグを検索値として「保存フラグ = 0」のままのデータがあるかの確認
	//　※データがなければ、申請ボタンを出現させるため
	// ======================================================== 
	public int selectApplicationButton(int expenseId){
		int selectApplicationButton = tdRepository.selectApplicationButton(expenseId);
		return selectApplicationButton;
	}
	
	//▶▶INSERT
	// ========================================================
	//データベースへ申請情報を保存するメソッド
	// ========================================================
	public T_DETAILS saveApplication(T_DETAILS applicationContent) {
		T_DETAILS savedContent = tdRepository.save(applicationContent);
		return savedContent;
	}
	
	//▶▶UPDATE
	// ========================================================
	// 申請IDを検索値として該当のデータの保存フラグを「1 = 保存済」に更新する
	// ========================================================
	public int updateOfSaveFlag(int expenseId) {
		int updateOfSaveFlag = tdRepository.updateOfSaveFlag(expenseId);
		return updateOfSaveFlag;
	}

	//▶▶DELETE
	// ========================================================
	// 申請IDと保存フラグで申請内容を検索し、保存フラグ=0の時はそのデータを削除する
	// ========================================================
	public int deleteBySaveFlag(int expenseId) {
		int deleteBySaveFlag = tdRepository.deleteBySaveFlag(expenseId);
		return deleteBySaveFlag;
	}
	
	// ========================================================
	// 「削除」ボタンが押された明細IDのデータを削除する
	// ========================================================
	public int deleteByDetailIds(List<Integer> detailIds) {
		int deleteDetailId = tdRepository.deleteByDetailIds(detailIds);
		return deleteDetailId;
	}

	// ========================================================
	// 申請IDのリストを検索値として保存フラグが「０」の明細情報を全削除する
	// ========================================================
	public int deleteBySaveFlagOfZero(List<String> expenseIds) {
		int deleteBySaveFlagOfZero = tdRepository.deleteBySaveFlagOfZero(expenseIds);
		return deleteBySaveFlagOfZero;
	}
	
	// ========================================================
	// 申請IDを検索値として紐づく明細情報を全削除する
	// ========================================================
	public int deleteByDetailIdOfExpenseId(int expenseId) {
		int deleteByDetailIdOfExpenseId = tdRepository.deleteByDetailIdOfExpenseId(expenseId);
		return deleteByDetailIdOfExpenseId;
	}
	
	// ========================================================
	// 申請IDを検索値として、利用日or開始日＋終了日のどちらも空欄になっている明細がないか確認する
	// ========================================================
	public int checkDate(int expenseId) {
		int checkDate = tdRepository.checkDate(expenseId);
		return checkDate;
	}
}
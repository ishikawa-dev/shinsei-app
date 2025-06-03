package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ApplicationCardDTO;
import com.example.demo.dto.HeaderUserDTO;
import com.example.demo.model.T_HEADER;

@Repository
public interface THeaderRepository extends JpaRepository<T_HEADER, Integer>, JpaSpecificationExecutor<T_HEADER> {
    
	//▶▶SELECT
	// ========================================================
	// 申請済かそうでないかの確認
	// ========================================================	 
	 @Query("SELECT COUNT(*) FROM T_HEADER WHERE expenseId = :expenseId AND expenseFlag = 1")
	 int checkAppStatus(@Param("expenseId") int expenseId);

	// ========================================================
	// ユーザーIDを検索値として申請フラグが「０」の申請IDの一覧を取得する
	// ========================================================
    @Query("SELECT t.expenseId FROM T_HEADER t WHERE t.expenseFlag = 0 AND t.userId = :userId")
    List<String> findExpenseIdsWithFlagZero(@Param("userId") String userId);
    
	// ========================================================
	// 申請IDを検索値としてヘッダー情報を取得する
	// ========================================================
    @Query("SELECT t FROM T_HEADER t WHERE t.expenseId = :expenseId")
    T_HEADER findHeaderByExpenseId(@Param("expenseId") int expenseId);
    
	// ========================================================
	// 承認ステータス「3＝申請中」のものの申請情報を取得する（まだ「承認/却下」ボタン未押下のものは更新一覧に表示する）
	// ========================================================
    @Query(value = """
    	    SELECT h.expense_id, h.expense_month, h.submission_date, u.name,
    	    	   COALESCE(SUM(d.amount), 0) AS totalAmount
    	    FROM T_HEADER h
    	    JOIN T_USERS u ON h.user_id = u.user_id
    	    LEFT JOIN T_DETAILS d ON h.expense_id = d.expense_id
    	    WHERE h.approval_status = 3
    	    GROUP BY h.expense_id, h.expense_month,h.submission_date,u.name
    	    """, nativeQuery = true)
    List<HeaderUserDTO> findApprovalStatusTHREE();
    
	// ========================================================
	// 検索結果でかえってきた申請IDを検索値としてユーザー名、申請タイトル、申請日、合算金額を取得する
	// ========================================================       
    @Query(value = """
    	    SELECT h.expense_id, h.expense_month, h.submission_date, u.name,
    	    	   COALESCE(SUM(d.amount), 0) AS totalAmount
    	    FROM T_HEADER h
    	    JOIN T_USERS u ON h.user_id = u.user_id
    	    LEFT JOIN T_DETAILS d ON h.expense_id = d.expense_id
    	    WHERE h.expense_id IN :expenseIds AND h.expense_flag = 1
    	    GROUP BY h.expense_id, h.expense_month,h.submission_date,u.name
    	    """, nativeQuery = true)
    List<HeaderUserDTO> findSearchResult(@Param("expenseIds") List<Integer> expenseIds);
    
	// ========================================================
	// 申請IDを検索値として承認ステータスが1＝却下、2＝承認済かどうかチェックする
	// ========================================================
    @Query("SELECT COUNT(*) FROM T_HEADER WHERE expenseId = :expenseId AND approvalStatus IN (1,2)")
    int checkApprovalStatus(@Param("expenseId") int expenseId);
    
	// ========================================================
	// ユーザーIDを検索値としてマイページの申請カードの中身を取得する
	// ========================================================
    @Query(value = """
    	    SELECT h.expense_id, h.expense_month, h.created_at, h.submission_date, h.payment_date,
    	           h.approval_status, COALESCE(SUM(d.amount), 0) AS totalAmount, h.user_id,
    	           h.dismissed_reason,h.expense_year
    	    FROM T_HEADER h
    	    LEFT JOIN T_DETAILS d ON h.expense_id = d.expense_id
    	    WHERE h.user_id = :user_id AND h.expense_year = 2025
    	    GROUP BY h.expense_id, h.expense_month, h.created_at, h.submission_date,
    	             h.payment_date, h.approval_status, h.user_id,h.dismissed_reason,h.expense_year
    	    ORDER BY h.expense_id DESC
    	    """, nativeQuery = true)
    	List<ApplicationCardDTO> findApplicationCards(@Param("user_id") String user_id);
   
	// ========================================================
	// ユーザーIDを検索値として申請フラグが「０」の申請IDの一覧を取得する
	// ========================================================
    @Query("SELECT t.expenseYear FROM T_HEADER t WHERE t.userId = :userId")
    List<Integer> findExpenseYear(@Param("userId") String userId);
    
	// ========================================================
	// ユーザーIDと選択年を検索値としてマイページの申請カードの中身を取得する
	// ========================================================
    @Query(value = """
    	    SELECT h.expense_id, h.expense_month, h.created_at, h.submission_date, h.payment_date,
    	           h.approval_status, COALESCE(SUM(d.amount), 0) AS totalAmount, h.user_id,
    	           h.dismissed_reason,h.expense_year
    	    FROM T_HEADER h
    	    LEFT JOIN T_DETAILS d ON h.expense_id = d.expense_id
    	    WHERE h.user_id = :user_id AND h.expense_year = :expense_year
    	    GROUP BY h.expense_id, h.expense_month, h.created_at, h.submission_date,
    	             h.payment_date, h.approval_status, h.user_id,h.dismissed_reason,h.expense_year
    	    ORDER BY h.expense_id DESC
    	    """, nativeQuery = true)
    	List<ApplicationCardDTO> findUpdateApplicationCards(@Param("user_id") String user_id,@Param("expense_year") int expense_year);

    
    
	//▶▶UPDATE
	// ========================================================
	// 「申請」ボタンが押された申請IDの申請フラグを「1 = 申請済」にする
	// ========================================================
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_HEADER SET expenseFlag = 1 WHERE expenseId = :expenseId")
	 int updateExpenseFlag(@Param("expenseId") int expenseId);
	 
	// ========================================================
	// 「申請」ボタンが押された申請IDの申請日をシステム日付に更新する
	// ========================================================
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_HEADER SET submissionDate = CURRENT_DATE WHERE expenseId = :expenseId")
	 int updateSubmissionDate(@Param("expenseId") int expenseId);
	 
	// ========================================================
	// 該当する申請IDの入力日をシステム日付に更新する
	// ========================================================
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_HEADER SET createdDate = CURRENT_DATE WHERE expenseId = :expenseId")
	 int updateCreatedDate(@Param("expenseId") int expenseId);
 
	// ========================================================
	// 「申請」ボタンが押された申請のステータスを「3 = 申請中」に更新する
	// ========================================================
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_HEADER SET approvalStatus = 3 WHERE expenseId = :expenseId")
	 int updateStutasOfTHREE(@Param("expenseId") int expenseId);
	 
	// ========================================================
	// 「承認」ボタンが押された申請のステータスを「2 = 承認済」に更新する
	// ========================================================
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_HEADER SET approvalStatus = 2 WHERE expenseId = :expenseId")
	 int updateStutasOfTWO(@Param("expenseId") int expenseId);
	 
	// ========================================================
	// 「却下」ボタンが押された申請のステータスを「1 = 却下済」に更新する
	// ========================================================
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_HEADER SET approvalStatus = 1 WHERE expenseId = :expenseId")
	 int updateStutasOfONE(@Param("expenseId") int expenseId);
 
	// ========================================================
	// 「支払」ボタンが押された申請の支払い日を更新する
	// ========================================================
	 //@Modifying
	 //@Transactional
	 //@Query("UPDATE T_HEADER SET paymentDate = :paymentDate WHERE expenseId = :expenseId")
	 //int updatePaymentDate(@Param("paymentDate") Date paymentDate,@Param("expenseId") int expenseId);
	 
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_HEADER SET paymentDate = :paymentDate WHERE expenseId = :expenseId")
	 int updatePaymentDate(@Param("paymentDate") LocalDate paymentDate, @Param("expenseId") int expenseId);

	// ========================================================
	// 「却下」ボタンが押された申請の却下理由を更新する
	// ========================================================
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_HEADER SET dismissedReason = :dismissedReason WHERE expenseId = :expenseId")
	 int updateDismissedReason(@Param("dismissedReason") String dismissedReason,@Param("expenseId") int expenseId);
 
	// ========================================================
	// 更新一覧から表示された申請の表示フラグを「1 = 表示済」に更新する
	// ========================================================
	 @Modifying
	 @Transactional
	 @Query("UPDATE T_HEADER SET displayFlag = 1 WHERE expenseId = :expenseId")
	 int updateDisplayFlag(@Param("expenseId") int expenseId);
	 
	//▶▶DELETE
	// ========================================================
	// 申請IDを検索値として紐づく明細情報を全削除する
	// ========================================================
	@Modifying
	@Transactional
	@Query("DELETE FROM T_HEADER WHERE expenseId = :expenseId")
	int deleteByExpenseIdOfExpenseId(@Param("expenseId") int expenseId);

}
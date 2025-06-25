package com.example.demo.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.T_DETAILS;

@Repository
public interface TDetailsRepository extends JpaRepository<T_DETAILS, Integer> {
	
	//▶▶SELECT
	// ========================================================
	// 申請IDと経費種別と保存フラグで申請内容を検索する
	// ========================================================
	@Query("SELECT t FROM T_DETAILS t WHERE t.expenseId = :expenseId AND t.expenseType = :expenseType ORDER BY t.usageDate ASC, t.validFrom ASC")
	List<T_DETAILS> findSavedDetails(@Param("expenseId") int expenseId, @Param("expenseType") int expenseType);
	
	// ========================================================
	// 申請IDを検索値として該当のデータの合算金額を取得
	// ========================================================
	 @Query("SELECT amount FROM T_DETAILS WHERE expenseId = :expenseId")
	 List<BigDecimal> totalAmountInfo(@Param("expenseId") Integer expenseId);
	 
	// ========================================================
	// 申請IDと保存フラグを検索値として「保存フラグ = 0」のままのデータがあるかの確認
	//　※データがなければ、申請ボタンを出現させるため
	// ======================================================== 
	@Query(value = "SELECT CASE " +
            "           WHEN COUNT(*) = SUM(CASE WHEN save_flag = 1 THEN 1 ELSE 0 END) " +
            "           THEN 1 " +
            "           ELSE 0 " +
            "       END AS all_saved " +
            "FROM T_DETAILS " +
            "WHERE expense_id = :expenseId", 
    nativeQuery = true)
	int selectApplicationButton(@Param("expenseId") Integer expenseId);
	
	// ========================================================
	// 申請IDを検索値として、利用日or開始日＋終了日のどちらも空欄になっている明細がないか確認する
	// ========================================================
	@Query("SELECT COUNT(t) FROM T_DETAILS t WHERE t.expenseId = :expenseId AND (t.usageDate IS NULL AND (t.validFrom IS NULL AND t.validUntil IS NULL))")
    int checkDate(@Param("expenseId") int expenseId);
	 
	
	//▶▶UPDATE
	// ========================================================
	// 申請IDを検索値として該当のデータの保存フラグを「1」に更新する
	// ======================================================== 
	@Modifying
    @Transactional
    @Query("UPDATE T_DETAILS SET saveFlag = 1 WHERE expenseId = :expenseId AND saveFlag = 0")
    int updateOfSaveFlag(@Param("expenseId") Integer expenseId);	
	
	
	//▶▶DELETE
	// ========================================================
	// 申請IDと保存フラグで申請内容を検索し、保存フラグ=0の時はそのデータを削除する
	// ========================================================
	@Modifying
    @Transactional
    @Query("DELETE FROM T_DETAILS WHERE expenseId = :expenseId AND saveFlag = 0")
    int deleteBySaveFlag(@Param("expenseId") Integer expenseId);

	// ========================================================
	// チェックボックスで選択された明細IDたちを検索値として該当のデータを削除し、削除できたデータ数を返す
	// ========================================================
	@Modifying
	@Transactional
	@Query("DELETE FROM T_DETAILS WHERE detailId IN :detailIds")
	int deleteByDetailIds(@Param("detailIds") List<Integer> detailIds);
	
	// ========================================================
	// 申請IDのリストを検索値として保存フラグが「０」の明細情報を全削除する
	// ========================================================
	@Modifying
	@Transactional
	@Query("DELETE FROM T_DETAILS t WHERE t.saveFlag = 0 AND t.expenseId IN :expenseIds")
	int deleteBySaveFlagOfZero(@Param("expenseIds") List<String> expenseIds);
	
	// ========================================================
	// 申請IDを検索値として紐づく明細情報を全削除する
	// ========================================================
	@Modifying
	@Transactional
	@Query("DELETE FROM T_DETAILS WHERE expenseId = :expenseId")
	int deleteByDetailIdOfExpenseId(@Param("expenseId") Integer expenseId);

}
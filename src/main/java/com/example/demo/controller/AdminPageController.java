package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.common.DisplayInfoHelper;
import com.example.demo.dto.HeaderUserDTO;
import com.example.demo.model.T_HEADER;
import com.example.demo.model.T_USERS;
import com.example.demo.repository.THeaderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.ExpenseIdService;
import com.example.demo.service.UserService;
import com.example.demo.specification.HeaderSpecification;

@Controller
public class AdminPageController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationService appService;
	
	@Autowired
	private ExpenseIdService expenseIdService;

	@Autowired
	private DisplayInfoHelper diHelper;
	
	@Autowired
	private THeaderRepository thRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/admin/mypage")
	public String getMypage(Model model,HttpSession session) {
		//セッションに入れている従業員一覧をmodelに追加
		List<T_USERS> employeeList = userService.employeeList();
		model.addAttribute("employeeList", employeeList);
		session.setAttribute("employeeList", employeeList);
		
		//申請中の一覧を取得
		List<HeaderUserDTO> applicationList = appService.findApprovalStatusTHREE();
		model.addAttribute("searchResult", applicationList);
		session.setAttribute("searchResult", applicationList);
        model.addAttribute("selectedStatus", 3);
		
		return "admin/mypage";
	}

    //★：更新一覧画面でリンクを押下
	@GetMapping("/admin/{id}")
	public String getDetailPage(@PathVariable("id") int expenseId,
							    Model model,HttpSession session) {
		
		//★：確認ページへの遷移（URLが押下されたとき）
		if(expenseId != 0) {
			
			//申請IDをセッションに追加
			session.setAttribute("expenseId", expenseId);
			
			//選択された申請IDのヘッダー情報を取得
			T_HEADER headerInfo = expenseIdService.getHeaderByExpenseId(expenseId);
			
			//選択された申請IDのユーザー名を取得
			String userId = headerInfo.getUserId();
			String userName = userRepository.getNameById(userId);
			
			//セッションとmodelにヘッダー情報と申請タイトルを追加して、HTMLに渡す
			model.addAttribute("headerInfo",headerInfo);
			session.setAttribute("headerInfo",headerInfo);
			model.addAttribute("expenseMonth", headerInfo.getExpenseMonth());
			session.setAttribute("userName",userName);
			
			//申請内容と合算金額の取得
			diHelper.setupDisplayInfo(model, session, expenseId);
			
			//表示フラグを「1=表示済」に更新する
			int updateDisplayFlag = appService.updateDisplayFlag(expenseId);
			
			//すでに「承認/却下」ボタンが押された申請かどうかの確認
			int checkApprovalStatus = appService.checkApprovalStatus(expenseId);
			if(checkApprovalStatus >= 1) { //「承認/却下」ボタンが押された申請
				model.addAttribute("checkApprovalStatus", checkApprovalStatus);
			}
			
			return "admin/details";
		}
		
		return "admin/mypage";
	}
	
	//★:管理者詳細ページの「支払」「却下」ボタン押下後
	@PostMapping("/admin/details")
	public String getApprovalOrDeismissed(@RequestParam(value = "paymentDate", required = false) LocalDate paymentDate,
		    							  @RequestParam(value = "rejectReason", required = false) String rejectReason,
		    							  HttpSession session,Model model) {
		
		//申請IDの取得
		int expenseId = (Integer) session.getAttribute("expenseId");
		
		if(paymentDate != null) {
			//支払日と承認ステータスの更新
			int updateStutasOfTWO = appService.updateStutasOfTWO(expenseId);
			int updateSubmissionDate = appService.updateSubmissionDate(paymentDate,expenseId);
			if(updateStutasOfTWO > 0 && updateSubmissionDate > 0) {
				model.addAttribute("confirmMessage", "支払日の登録が完了しました");
			}
		}else if(rejectReason != null) {
			//却下理由と承認ステータスの更新
			int updateStutasOfONE = appService.updateStutasOfONE(expenseId);
			int updateDismissedReason = appService.updateDismissedReason(rejectReason, expenseId);
			if(updateStutasOfONE > 0 && updateDismissedReason > 0) {
				model.addAttribute("confirmMessage", "却下されました");
			}
		}
		
		//選択された申請IDのヘッダー情報を取得
		T_HEADER headerInfo = expenseIdService.getHeaderByExpenseId(expenseId);
		
		//セッションとmodelにヘッダー情報と申請タイトルを追加して、HTMLに渡す
		model.addAttribute("headerInfo",headerInfo);
		session.setAttribute("headerInfo",headerInfo);
		model.addAttribute("expenseMonth", headerInfo.getExpenseMonth());
		
		//申請内容と合算金額の取得
		diHelper.setupDisplayInfo(model, session, expenseId);
		
		//すでに「承認/却下」ボタンが押された申請かどうかの確認
		int checkApprovalStatus = appService.checkApprovalStatus(expenseId);
		if(checkApprovalStatus >= 1) { //「承認/却下」ボタンが押された申請
			model.addAttribute("checkApprovalStatus", checkApprovalStatus);
		}
		
		return "admin/details";
	}
	
	//★:検索ボタン押下
	@PostMapping("/admin/search")
	public String search(@RequestParam("userId") String userId,
						 @RequestParam("submissionDateFrom") String submissionDateFrom,
						 @RequestParam("submissionDateTo") String submissionDateTo,
						 @RequestParam("approvalStatus") String approvalStatus,
						 Model model,HttpSession session,
						 @RequestParam("action") String action) {
		
		if("reset".equals(action)) {
			session.removeAttribute("searchResult");
			return "admin/mypage";
		}
	
	    // いずれかの検索条件が入っているかをチェック
	    boolean hasCondition = (userId != null && !userId.isEmpty())
	                        || (submissionDateFrom != null && !submissionDateFrom.isEmpty())
	                        || (submissionDateTo != null && !submissionDateTo.isEmpty())
	                        || (approvalStatus != null && !approvalStatus.isEmpty());
		
	    List<T_HEADER> results = null;
	    
	    if (hasCondition) {
	        // Specificationを呼び出して検索条件を作る
	        Specification<T_HEADER> spec = HeaderSpecification.searchByConditions(userId, submissionDateFrom,submissionDateTo,approvalStatus);

	        // リポジトリのfindAllにSpecificationを渡して検索
	        results = thRepository.findAll(spec);
	        
	        //申請IDを取得してListに格納する
	        List<Integer> expenseIdList = new ArrayList<>();
	        for (T_HEADER header : results) {
	            expenseIdList.add(header.getExpenseId());
	        }
	        
	        //申請情報（申請ID、タイトル、申請日、名前）の取得
	        List<HeaderUserDTO> searchResult = appService.findSearchResult(expenseIdList);

	        // 検索結果をモデルにセットしてビューに渡す
	        model.addAttribute("searchResult", searchResult);
	        session.setAttribute("searchResult", searchResult);
	        model.addAttribute("selectedUserId", userId);
	        model.addAttribute("submissionDateFrom", submissionDateFrom);
	        model.addAttribute("submissionDateTo", submissionDateTo);
	        model.addAttribute("selectedStatus", approvalStatus);
	    } else {
	        // 全件取得
	    	results = thRepository.findAll();
	    	
	        //申請IDを取得してListに格納する
	        List<Integer> expenseIdList = new ArrayList<>();
	        for (T_HEADER header : results) {
	            expenseIdList.add(header.getExpenseId());
	        }
	        
	        //申請情報（申請ID、タイトル、申請日、名前）の取得
	        List<HeaderUserDTO> searchResult = appService.findSearchResult(expenseIdList);
	    	
		    // 結果をモデルに追加してビューに渡す
		    model.addAttribute("searchResult", searchResult);
		    session.setAttribute("searchResult", searchResult);
	    }

		return "/admin/mypage";
	}
	
}
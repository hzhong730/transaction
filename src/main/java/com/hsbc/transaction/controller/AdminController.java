package com.hsbc.transaction.controller;

import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.model.vo.BondTransactionVO;
import com.hsbc.transaction.service.TransactionQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private TransactionQueryService transactionQueryService;

    @GetMapping("/viewAllBondTransactions")
    public String getAllUsers(Model model) {
        List<BondTransactionVO> data = transactionQueryService.loadAllBondTransactions();
        model.addAttribute("transaction", data);
        return "viewAllBondTransactions";
    }
}

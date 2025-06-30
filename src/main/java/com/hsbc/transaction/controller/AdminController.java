package com.hsbc.transaction.controller;

import com.hsbc.transaction.model.vo.BondTransactionVO;
import com.hsbc.transaction.service.TransactionQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private TransactionQueryService transactionQueryService;

    @GetMapping("/viewAllTransactions/{type}")
    public String getAllTransactions(Model model, @PathVariable String type) {
        List<BondTransactionVO> data = transactionQueryService.loadAllTransactions(type);
        model.addAttribute("transaction", data);
        return "viewAllBondTransactions";
    }

    @GetMapping("/viewCustomersTransactions/{customer}")
    public String getTransactionsByCustomer(Model model, @PathVariable String customer) {
        List<BondTransactionVO> data = transactionQueryService.loadTransactionsByCustomer(customer);
        model.addAttribute("transaction", data);
        return "viewAllBondTransactions";
    }

    @GetMapping("/viewTransactionsHistory/{tradeId}")
    public String getTransactionsHistory(Model model, @PathVariable String tradeId) {
        List<BondTransactionVO> data = transactionQueryService.loadTransactionsHistory(tradeId);
        model.addAttribute("transaction", data);
        return "viewAllBondTransactions";
    }
}

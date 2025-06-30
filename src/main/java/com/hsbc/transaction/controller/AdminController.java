package com.hsbc.transaction.controller;

import com.hsbc.transaction.model.vo.BondTransactionVO;
import com.hsbc.transaction.model.vo.PageInfo;
import com.hsbc.transaction.service.TransactionQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    @Autowired
    private TransactionQueryService transactionQueryService;

    @GetMapping("/viewAllTransactions/{type}")
    public String getAllTransactions(Model model, @PathVariable String type,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        List<BondTransactionVO> data = transactionQueryService.loadAllTransactions(type);
        addPage(data, model, page, size);
        return "viewAllBondTransactions";
    }

    @GetMapping("/viewCustomersTransactions/{customer}")
    public String getTransactionsByCustomer(Model model, @PathVariable String customer,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        List<BondTransactionVO> data = transactionQueryService.loadTransactionsByCustomer(customer);
        addPage(data, model, page, size);
        return "viewAllBondTransactions";
    }

    @GetMapping("/viewTransactionsHistory/{tradeId}")
    public String getTransactionsHistory(Model model, @PathVariable String tradeId,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        List<BondTransactionVO> data = transactionQueryService.loadTransactionsHistory(tradeId);
        addPage(data, model, page, size);
        return "viewAllBondTransactions";
    }

    private void addPage(List<BondTransactionVO> data, Model model, int page, int size) {
        int totalItems = data.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<BondTransactionVO> pageData = data.stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .toList();
        PageInfo pageInfo = new PageInfo(pageData,page,size,totalPages,totalItems);
        model.addAttribute("pageInfo", pageInfo);
    }
}

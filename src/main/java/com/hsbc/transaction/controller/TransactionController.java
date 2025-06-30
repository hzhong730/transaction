package com.hsbc.transaction.controller;

import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.model.vo.ResultVO;
import com.hsbc.transaction.service.TransactionManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionManageService transactionManageService;
    @PostMapping("/addTransaction")
    public ResultVO addTransaction(@RequestBody TransactionDTO transaction) {
        return transactionManageService.add(transaction);
    }

    @PutMapping("/updateTransaction")
    public ResultVO updateTransaction(@RequestBody TransactionDTO transaction) {
        return transactionManageService.update(transaction);
    }

    @DeleteMapping("/deleteTransaction/{tradeId}")
    public ResultVO deleteTransaction(@PathVariable String tradeId) {
        return transactionManageService.delete(tradeId);
    }

}

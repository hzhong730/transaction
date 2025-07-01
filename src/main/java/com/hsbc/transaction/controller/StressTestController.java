package com.hsbc.transaction.controller;

import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.model.vo.ResultVO;
import com.hsbc.transaction.repository.TransactionRepository;
import com.hsbc.transaction.service.TransactionCacheService;
import com.hsbc.transaction.service.TransactionManageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stress")
public class StressTestController {
    private final List<String> directions = new ArrayList<>();
    private final List<Long> amounts = new ArrayList<>();
    private final List<Double> prices = new ArrayList<>();

    @Autowired
    private TransactionManageService transactionManageService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private  TransactionCacheService transactionCacheService;



    @PostConstruct
    public void init() {
        directions.add("BID");
        directions.add("ASK");
        amounts.add(100L);
        amounts.add(200L);
        amounts.add(250L);
        prices.add(90.0);
        prices.add(95.0);
        prices.add(97.0);
        prices.add(99.0);
    }

    @PostMapping("/addData/{number}")
    public ResultVO addTransaction(@RequestBody TransactionDTO transactionTemplate, @PathVariable int number,
                                   @RequestParam String user) {
        if (!"hzhong730".equals(user))
            return ResultVO.buildFailure("this user is not allowed to run stress test mode");
        for (int i=0;i<number;i++) {
            TransactionDTO copy = TransactionRepository.copy(transactionTemplate);
            copy.setId(null);
            copy.setTradeID(null);
            copy.setDirection(directions.get(i % 2));
            copy.setAmount(amounts.get(i % 3));
            copy.setPrice(prices.get(i % 4));
            transactionManageService.add(copy);
        }
        return ResultVO.buildSuccess();
    }

    @GetMapping("/monitor")
    public String mointor() {
        String dbStatistic = transactionRepository.monitor();
        String cacheStatistic = transactionCacheService.monitor();
        return dbStatistic + "; " + cacheStatistic;
    }
}

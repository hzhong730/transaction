package com.hsbc.transaction.service;

import com.hsbc.transaction.model.vo.BondTransactionVO;
import com.hsbc.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TransactionQueryService {
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Autowired
    private TransactionRepository transactionRepository;

    public List<BondTransactionVO> loadAllTransactions(String type) {
        //TODO enrich BOND product info like coupon, start, maturity with Product service
        return  transactionRepository.getAllTransactionsByType(type).stream()
                .map(r->new BondTransactionVO(r, dateFormat)).toList();
    }

    public List<BondTransactionVO> loadTransactionsByCustomer(String customer) {
        return  transactionRepository.getAllTransactionsByCustomer(customer).stream()
                .map(r->new BondTransactionVO(r, dateFormat)).toList();
    }


}

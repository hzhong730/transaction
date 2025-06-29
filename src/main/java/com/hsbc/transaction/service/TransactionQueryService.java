package com.hsbc.transaction.service;

import com.hsbc.transaction.model.TransactionDTO;
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

    public List<BondTransactionVO> loadAllBondTransactions() {
        return  transactionRepository.getAllTransactions().stream()
                .map(r->new BondTransactionVO(r, dateFormat)).toList();
    }
}

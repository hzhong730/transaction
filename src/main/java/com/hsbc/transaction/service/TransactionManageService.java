package com.hsbc.transaction.service;

import com.hsbc.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionManageService {

    @Autowired
    private TransactionRepository transactionRepository;
}

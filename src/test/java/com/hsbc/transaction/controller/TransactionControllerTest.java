package com.hsbc.transaction.controller;

import com.hsbc.transaction.TransactionDTOTestUtils;
import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.service.TransactionManageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @InjectMocks
    private TransactionController target;
    @Mock
    private TransactionManageService transactionManageService;

    @Test
    public void test() {
        TransactionDTO data = TransactionDTOTestUtils.aTransaction("BOND", "t1");
        target.addTransaction(data);
        target.updateTransaction(data);
        target.deleteTransaction("t1");
    }
}

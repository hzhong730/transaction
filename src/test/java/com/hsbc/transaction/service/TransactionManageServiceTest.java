package com.hsbc.transaction.service;

import com.hsbc.transaction.TransactionDTOTestUtils;
import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.model.vo.ResultVO;
import com.hsbc.transaction.repository.TransactionRepository;
import com.hsbc.transaction.service.validator.TransactionValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TransactionManageServiceTest {
    @InjectMocks
    private TransactionManageService target;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionValidator transactionValidator;

    @Test
    public void testAddFail() {
        TransactionDTO data = TransactionDTOTestUtils.aTransaction("FX", "t01");
        List<String> validateResult = new ArrayList<>();
        String msg = "Sorry, BOND is the only type supported now";
        validateResult.add(msg);
        Mockito.when(transactionValidator.validateTransactionDTO(data)).thenReturn(validateResult);
        ResultVO result = target.add(data);
        Assertions.assertEquals(ResultVO.INVALID, result.getResult());
        Assertions.assertEquals(msg, result.getMessage());
    }

    @Test
    public void testAdd() {
        TransactionDTO data = TransactionDTOTestUtils.aTransaction("BOND", "t01");
        Mockito.when(transactionValidator.validateTransactionDTO(data)).thenReturn(new ArrayList<>());
        ResultVO result = target.add(data);
        Assertions.assertEquals(ResultVO.SUCCESS, result.getResult());
    }

    @Test
    public void testUpdateFail1() {
        TransactionDTO data = TransactionDTOTestUtils.aTransaction("FX", "");
        ResultVO result = target.update(data);
        Assertions.assertEquals(ResultVO.INVALID, result.getResult());
        Assertions.assertEquals("Trade id is missing in transaction", result.getMessage());
    }

    @Test
    public void testUpdateFail2() {
        TransactionDTO data = TransactionDTOTestUtils.aTransaction("FX", "t02");
        List<String> validateResult = new ArrayList<>();
        String msg = "Sorry, BOND is the only type supported now";
        validateResult.add(msg);
        Mockito.when(transactionValidator.validateTransactionDTO(data)).thenReturn(validateResult);
        ResultVO result = target.update(data);
        Assertions.assertEquals(ResultVO.INVALID, result.getResult());
        Assertions.assertEquals(msg, result.getMessage());
    }

    @Test
    public void testUpdate() {
        TransactionDTO data = TransactionDTOTestUtils.aTransaction("BOND", "t01");
        Mockito.when(transactionValidator.validateTransactionDTO(data)).thenReturn(new ArrayList<>());
        ResultVO result = target.update(data);
        Assertions.assertEquals(ResultVO.SUCCESS, result.getResult());
    }

    @Test
    public void testDelete() {
        ResultVO result = target.delete("t01");
        Assertions.assertEquals(ResultVO.SUCCESS, result.getResult());
    }

}

package com.hsbc.transaction.service;

import com.hsbc.transaction.TransactionDTOTestUtils;
import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.model.vo.BondTransactionVO;
import com.hsbc.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TransactionQueryServiceTest {
    @InjectMocks
    private TransactionQueryService target;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionCacheService cacheService;

    @Test
    public void testLoadByTypeCache() {
        target.afterSingletonsInstantiated();
        List<BondTransactionVO> cached = new ArrayList<>();
        cached.add(TransactionDTOTestUtils.aTransactionVo("BOND", "t01"));
        Mockito.when(cacheService.getTransactionsFromCache("BOND")).thenReturn(cached);
        List<BondTransactionVO> result = target.loadAllTransactions("BOND");
        Assertions.assertEquals(cached, result);
    }

    @Test
    public void testLoadByTypeDB() {
        Mockito.when(cacheService.getTransactionsFromCache("BOND")).thenReturn(new ArrayList<>());
        TransactionDTO d1 = TransactionDTOTestUtils.aTransaction("BOND", "t01");
        List<TransactionDTO> data = new ArrayList<>();
        data.add(d1);
        Mockito.when(transactionRepository.getAllTransactionsByType("BOND")).thenReturn(data);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Mockito.when(cacheService.getDateFormat()).thenReturn(dateFormat);
        List<BondTransactionVO> result = target.loadAllTransactions("BOND");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("t01", result.get(0).getTradeID());
    }

    @Test
    public void testLoadByCustomerCache() {
        List<BondTransactionVO> cached = new ArrayList<>();
        cached.add(TransactionDTOTestUtils.aTransactionVo("BOND", "t01"));
        Mockito.when(cacheService.getTransactionsFromCacheByCustomer("C")).thenReturn(cached);
        List<BondTransactionVO> result = target.loadTransactionsByCustomer("C");
        Assertions.assertEquals(cached, result);
    }

    @Test
    public void testLoadByCustomerDB() {
        Mockito.when(cacheService.getTransactionsFromCacheByCustomer("C")).thenReturn(new ArrayList<>());
        TransactionDTO d1 = TransactionDTOTestUtils.aTransaction("BOND", "t01");
        List<TransactionDTO> data = new ArrayList<>();
        data.add(d1);
        Mockito.when(transactionRepository.getAllTransactionsByCustomer("C")).thenReturn(data);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Mockito.when(cacheService.getDateFormat()).thenReturn(dateFormat);
        List<BondTransactionVO> result = target.loadTransactionsByCustomer("C");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("t01", result.get(0).getTradeID());

    }

    @Test
    public void testLoadHistory() {
        TransactionDTO d1 = TransactionDTOTestUtils.aTransaction("BOND", "t01");
        List<TransactionDTO> data = new ArrayList<>();
        data.add(d1);
        Mockito.when(transactionRepository.getDataHistory("t01")).thenReturn(data);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Mockito.when(cacheService.getDateFormat()).thenReturn(dateFormat);
        List<BondTransactionVO> result = target.loadTransactionsHistory("t01");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("t01", result.get(0).getTradeID());
    }
}

package com.hsbc.transaction.service;

import com.hsbc.transaction.TransactionDTOTestUtils;
import com.hsbc.transaction.model.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TransactionCacheServiceTest {
    @InjectMocks
    private TransactionCacheService target;

    @Test
    public void testInit() {
        List<TransactionDTO> dbData = prepareInitData();
        target.initCache(dbData);
        Assertions.assertEquals(3, target.getTransactionsFromCache("BOND").size());
        Assertions.assertEquals(2,target.getTransactionsFromCacheByCustomer("C").size());
        Assertions.assertEquals(1,target.getTransactionsFromCacheByCustomer("B").size());
    }

    private List<TransactionDTO> prepareInitData() {
        TransactionDTO d1 = TransactionDTOTestUtils.aTransaction("BOND", "t01");
        TransactionDTO d2 = TransactionDTOTestUtils.aTransaction("BOND", "t02");
        d2.setCounterParty2("B");
        TransactionDTO d3 = TransactionDTOTestUtils.aTransaction("BOND", "t03");
        d3.setCounterParty1("A");
        List<TransactionDTO> dbData = new ArrayList<>();
        dbData.add(d1);
        dbData.add(d2);
        dbData.add(d3);
        return dbData;
    }

    @Test
    public void testAdd() {
        List<TransactionDTO> dbData = prepareInitData();
        target.initCache(dbData);
        TransactionDTO newData = TransactionDTOTestUtils.aTransaction("BOND", "x01");
        target.addTransactionCache(newData);
        Assertions.assertEquals(4, target.getTransactionsFromCache("BOND").size());
        Assertions.assertEquals(3, target.getTransactionsFromCacheByCustomer("C").size());
        Assertions.assertEquals(3, target.getTransactionsFromCacheByCustomer("D").size());
        Assertions.assertEquals(1, target.getTransactionsFromCacheByCustomer("A").size());
    }

    @Test
    public void testUpdate() {
        List<TransactionDTO> dbData = prepareInitData();
        target.initCache(dbData);
        TransactionDTO update1 = TransactionDTOTestUtils.aTransaction("BOND", dbData.get(0).getTradeID());
        update1.setAmount(300L);
        update1.setCounterParty1("X");
        update1.setCounterParty2("Y");
        target.updateTransactionCache(update1, dbData.get(0));
        Assertions.assertEquals(3, target.getTransactionsFromCache("BOND").size());
        Assertions.assertEquals(1, target.getTransactionsFromCacheByCustomer("C").size());
        Assertions.assertEquals(1, target.getTransactionsFromCacheByCustomer("D").size());
        Assertions.assertEquals(1, target.getTransactionsFromCacheByCustomer("X").size());
        Assertions.assertEquals(1, target.getTransactionsFromCacheByCustomer("Y").size());
        Assertions.assertEquals("300", target.getByTradeId(update1.getTradeID()).getAmount());
    }

    @Test
    public void testDelete() {
        List<TransactionDTO> dbData = prepareInitData();
        target.initCache(dbData);
        target.deleteTransactionCache(dbData.get(0));
        Assertions.assertEquals(2, target.getTransactionsFromCache("BOND").size());
        Assertions.assertEquals(1, target.getTransactionsFromCacheByCustomer("C").size());
        Assertions.assertEquals(1, target.getTransactionsFromCacheByCustomer("D").size());
        Assertions.assertNull(target.getByTradeId(dbData.get(0).getTradeID()));
    }
}

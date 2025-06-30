package com.hsbc.transaction.repository;

import com.hsbc.transaction.TransactionDTOTestUtils;
import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.service.TransactionCacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionRepositoryTest {
    @InjectMocks
    private TransactionRepository target;
    @Mock
    private TransactionCacheService transactionCacheService;

    @BeforeEach
    public void init() {
        target.init();
    }

    @Test
    public void testInsert() {
        TransactionDTO d1 = TransactionDTOTestUtils.aTransaction("BOND", "a001");
        TransactionDTO d2 = TransactionDTOTestUtils.aTransaction("BOND", "a002");
        TransactionDTO d3 = TransactionDTOTestUtils.aTransaction("BOND", "a003");
        target.insert(d1);
        target.insert(d2);
        target.insert(d3);
        Assertions.assertEquals(4, target.getAllTransactions().size());
        Assertions.assertEquals(3, target.getAllTransactionsByCustomer("C").size());
        Assertions.assertEquals(3, target.getAllTransactionsByCustomer("D").size());
        Assertions.assertEquals(1, target.getAllTransactionsByCustomer("B").size());
        Assertions.assertEquals(1, target.getAllTransactionsByCustomer("A").size());
        Assertions.assertEquals(4, target.getAllTransactionsByType("BOND").size());
        Assertions.assertEquals(d3, target.getActiveData(d3.getTradeID()));
    }

    @Test
    public void testUpdate() {
        TransactionDTO d1 = TransactionDTOTestUtils.aTransaction("BOND", "a001");
        target.insert(d1);
        Assertions.assertEquals(100L, target.getActiveData(d1.getTradeID()).getAmount());
        TransactionDTO d1updated = TransactionDTOTestUtils.aTransaction("BOND", d1.getTradeID());
        d1updated.setAmount(50L);
        target.update(d1updated);
        Assertions.assertEquals(50L, target.getActiveData(d1.getTradeID()).getAmount());
        Assertions.assertEquals(2, target.getDataHistory(d1.getTradeID()).size());
    }

    @Test
    public void testDelete() {
        TransactionDTO d1 = TransactionDTOTestUtils.aTransaction("BOND", "a001");
        target.insert(d1);
        target.delete(d1.getTradeID());
        Assertions.assertNull(target.getActiveData(d1.getTradeID()));
        Assertions.assertEquals(2, target.getDataHistory(d1.getTradeID()).size());
    }

    @Test
    public void testUpdateFail() {
        TransactionDTO d1 = TransactionDTOTestUtils.aTransaction("BOND", "a001");
        target.insert(d1);
        TransactionDTO d1updated = TransactionDTOTestUtils.aTransaction("BOND", "otherID");
        Assertions.assertThrows(Exception.class, ()->{target.update(d1updated);});
    }

    @Test
    public void testDeleteFail() {
        TransactionDTO d1 = TransactionDTOTestUtils.aTransaction("BOND", "a001");
        target.insert(d1);
        Assertions.assertThrows(Exception.class, ()->{target.delete("Other");});
    }
}

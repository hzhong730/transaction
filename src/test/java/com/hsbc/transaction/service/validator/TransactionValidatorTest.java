package com.hsbc.transaction.service.validator;

import com.hsbc.transaction.TransactionDTOTestUtils;
import com.hsbc.transaction.model.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TransactionValidatorTest {
    @InjectMocks
    private TransactionValidator target;

    @Test
    public void test1() {
        TransactionDTO data = TransactionDTOTestUtils.aTransaction("BOND", "t01");
        List<String> result =  target.validateTransactionDTO(data);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void test2() {
        TransactionDTO data = new TransactionDTO();
        List<String> result =  target.validateTransactionDTO(data);
        Assertions.assertEquals(7, result.size());
    }

}

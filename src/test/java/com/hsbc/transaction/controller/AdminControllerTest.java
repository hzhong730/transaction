package com.hsbc.transaction.controller;

import com.hsbc.transaction.model.vo.BondTransactionVO;
import com.hsbc.transaction.service.TransactionQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    @InjectMocks
    private AdminController target;
    @Mock
    private TransactionQueryService transactionQueryService;

    @Test
    public void testGetAllTransactions() {
        Model model = Mockito.mock(Model.class);
        BondTransactionVO vo = Mockito.mock(BondTransactionVO.class);
        List<BondTransactionVO> vos = new ArrayList<>();
        for (int i=0;i<66;i++) {
            vos.add(vo);
        }
        Mockito.when(transactionQueryService.loadAllTransactions("BOND")).thenReturn(vos);
        Assertions.assertEquals("viewAllBondTransactions", target.getAllTransactions(model, "BOND", 1,10));
    }

    @Test
    public void testGetTransactionsByCustomer() {
        Model model = Mockito.mock(Model.class);
        BondTransactionVO vo = Mockito.mock(BondTransactionVO.class);
        List<BondTransactionVO> vos = new ArrayList<>();
        for (int i=0;i<20;i++) {
            vos.add(vo);
        }
        Mockito.when(transactionQueryService.loadTransactionsByCustomer("C1")).thenReturn(vos);
        Assertions.assertEquals("viewAllBondTransactions", target.getTransactionsByCustomer(model, "C1", 2,10));
    }

    @Test
    public void testGetTransactionsHistory() {
        Model model = Mockito.mock(Model.class);
        BondTransactionVO vo = Mockito.mock(BondTransactionVO.class);
        List<BondTransactionVO> vos = new ArrayList<>();
        for (int i=0;i<7;i++) {
            vos.add(vo);
        }
        Mockito.when(transactionQueryService.loadTransactionsHistory("t01")).thenReturn(vos);
        Assertions.assertEquals("viewAllBondTransactions", target.getTransactionsHistory(model, "t01",1,10));
    }
}

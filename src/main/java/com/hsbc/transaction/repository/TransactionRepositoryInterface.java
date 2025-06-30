package com.hsbc.transaction.repository;

import com.hsbc.transaction.model.TransactionDTO;

import java.util.List;

public interface TransactionRepositoryInterface {
    List<TransactionDTO> getAllTransactionsByType(String type);
    List<TransactionDTO> getAllTransactionsByCustomer(String customer);
    TransactionDTO insert(TransactionDTO data);
    TransactionDTO getActiveData(String tradeId);
    List<TransactionDTO> getDataHistory(String tradeId);
    TransactionDTO update(TransactionDTO data);
    void delete(String tradeId);
}

package com.hsbc.transaction.service;

import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.model.vo.ResultVO;
import com.hsbc.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionManageService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionManageService.class);

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionValidator transactionValidator;

    public ResultVO add(TransactionDTO data) {
        List<String> validateResult = transactionValidator.validateTransactionDTO(data);
        if (!validateResult.isEmpty()) {
            logger.warn("Invalid Transaction insertion: {}", validateResult);
            return ResultVO.buildInvalid(String.join(";", validateResult));
        }
        try {
            transactionRepository.insert(data);
        } catch (Exception e) {
            logger.error("Transaction Insert failed", e);
            ResultVO.buildFailure(e.getMessage());
        }
        return ResultVO.buildSuccess();
    }

    public ResultVO update(TransactionDTO data) {
        if (data.getTradeID() == null || data.getTradeID().isBlank()) {
            String msg = "Trade id is missing in transaction";
            logger.warn(msg);
            return ResultVO.buildInvalid(msg);
        }
        List<String> validateResult = transactionValidator.validateTransactionDTO(data);
        if (!validateResult.isEmpty()) {
            logger.warn("Invalid Transaction modification: {}", validateResult);
            return ResultVO.buildInvalid(String.join(";", validateResult));
        }
        try {
            transactionRepository.update(data);
        } catch (Exception e) {
            logger.error("Transaction update failed", e);
            return ResultVO.buildFailure(e.getMessage());
        }
        return ResultVO.buildSuccess();
    }

    public ResultVO delete(String tradeId) {
        try {
            transactionRepository.delete(tradeId);
        } catch (Exception e) {
            logger.error("Transaction delete failed", e);
            return ResultVO.buildFailure(e.getMessage());
        }
        return ResultVO.buildSuccess();
    }
}

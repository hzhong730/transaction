package com.hsbc.transaction.service;

import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.model.vo.BondTransactionVO;
import com.hsbc.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionQueryService implements SmartInitializingSingleton {
    private static final Logger logger = LoggerFactory.getLogger(TransactionQueryService.class);
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionCacheService cacheService;
    public List<BondTransactionVO> loadAllTransactions(String type) {
        //TODO enrich BOND product info like coupon, start, maturity with Product service
        List<BondTransactionVO> cached = cacheService.getTransactionsFromCache(type);
        if (!cached.isEmpty()) {
            logger.info("loadAllTransactions for type {} from cache, size {}", type, cached.size());
            return cached;
        }
        List<BondTransactionVO> vos = transactionRepository.getAllTransactionsByType(type).stream()
                .map(r->new BondTransactionVO(r, cacheService.getDateFormat())).collect(Collectors.toList());
        logger.info("loadAllTransactions for type {} from DB, size {}", type, cached.size());
        vos.forEach(vo->cacheService.putVoToCache(vo));
        Collections.reverse(vos);
        return vos;
    }

    public List<BondTransactionVO> loadTransactionsByCustomer(String customer) {
        List<BondTransactionVO> cached = cacheService.getTransactionsFromCacheByCustomer(customer);
        if (!cached.isEmpty()) {
            logger.info("loadTransactionsByCustomer for customer {} from cache, size {}", customer, cached.size());
            return cached;
        }
        List<BondTransactionVO> vos = transactionRepository.getAllTransactionsByCustomer(customer).stream()
                .map(r->new BondTransactionVO(r, cacheService.getDateFormat())).collect(Collectors.toList());
        logger.info("loadTransactionsByCustomer for customer {} from DB, size {}", customer, cached.size());
        vos.forEach(vo->cacheService.putVoToCache(vo));
        Collections.reverse(vos);
        return vos;
    }

    public List<BondTransactionVO> loadTransactionsHistory(String tradeId) {
        return  transactionRepository.getDataHistory(tradeId).stream()
                .map(r->new BondTransactionVO(r, cacheService.getDateFormat())).toList();
    }

    @Override
    public void afterSingletonsInstantiated() {
        List<TransactionDTO> allActiveDataFromDB = transactionRepository.getAllTransactions();
        cacheService.initCache(allActiveDataFromDB);
    }
}

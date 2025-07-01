package com.hsbc.transaction.service;

import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.model.vo.BondTransactionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class TransactionCacheService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionCacheService.class);
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    //key: transaction ids Value: BondTransactionVO
    private final Map<String, BondTransactionVO> bondTransactionCache = new ConcurrentHashMap<>();
    //key: customer value: transaction ids
    private final Map<String, Set<String>> customersTradeCache = new ConcurrentHashMap<>();

    public void initCache(List<TransactionDTO> data) {
        logger.info("init cache for data. size {}", data.size());
        for (TransactionDTO dto : data) {
            putToCache(dto);
        }
    }

    public List<BondTransactionVO> getTransactionsFromCache(String type) {
        if ("BOND".equalsIgnoreCase(type)) {
            return bondTransactionCache.values().stream()
                    .sorted((r1,r2)->(r2.getUpdatedOn().compareTo(r1.getUpdatedOn())))
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    public List<BondTransactionVO> getTransactionsFromCacheByCustomer(String customer) {
        Set<String> tradeIds = customersTradeCache.getOrDefault(customer, new HashSet<>());
        return tradeIds.stream().map(bondTransactionCache::get).filter(Objects::nonNull)
                .sorted((r1,r2)->(r2.getUpdatedOn().compareTo(r1.getUpdatedOn())))
                .toList();
    }

    public BondTransactionVO getByTradeId(String tradeID) {
        return bondTransactionCache.get(tradeID);
    }

    private void putToCache(TransactionDTO dto) {
        BondTransactionVO vo = new BondTransactionVO(dto, dateFormat);
        putVoToCache(vo);
    }

    public void putVoToCache(BondTransactionVO vo) {
        if ("BOND".equals(vo.getType())) {
            bondTransactionCache.put(vo.getTradeID(), vo);
        }
        initCustomerCache(vo.getCounterParty1(), vo.getTradeID());
        initCustomerCache(vo.getCounterParty2(), vo.getTradeID());
    }

    private void initCustomerCache(String customer, String tradeId) {
        if (customersTradeCache.containsKey(customer)) {
            customersTradeCache.get(customer).add(tradeId);
        } else {
            Set<String> tradeIds = new CopyOnWriteArraySet<>();
            tradeIds.add(tradeId);
            customersTradeCache.put(customer, tradeIds);
        }
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void addTransactionCache(TransactionDTO data) {
        putToCache(data);
    }

    public void updateTransactionCache(TransactionDTO data, TransactionDTO prev) {
        customersTradeCache.getOrDefault(prev.getCounterParty1(), new HashSet<>()).remove(prev.getTradeID());
        customersTradeCache.getOrDefault(prev.getCounterParty2(), new HashSet<>()).remove(prev.getTradeID());
        putToCache(data);

    }

    public void deleteTransactionCache(TransactionDTO data) {
        bondTransactionCache.remove(data.getTradeID());
        customersTradeCache.getOrDefault(data.getCounterParty1(), new HashSet<>()).remove(data.getTradeID());
        customersTradeCache.getOrDefault(data.getCounterParty2(), new HashSet<>()).remove(data.getTradeID());
    }

    public String monitor() {
        return String.format("main cache size %s, customer cache size %s",
                bondTransactionCache.size(), customersTradeCache.size());
    }
}

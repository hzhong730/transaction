package com.hsbc.transaction.repository;

import com.hsbc.transaction.model.DataEntity;
import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.service.TransactionCacheService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TransactionRepository implements TransactionRepositoryInterface {
    //as we don't need to store data to db, write a repository class to simulate spring data
    private final List<TransactionDTO> dbStore = new CopyOnWriteArrayList<>();
    //simulate trade id level lock
    private final Map<String, Object> tradeIdLevelLock = new ConcurrentHashMap<>();

    @Autowired
    private TransactionCacheService transactionCacheService;

    @Override
    public List<TransactionDTO> getAllTransactions() {
        return dbStore.stream().filter(r->!r.isDeleted()).filter(DataEntity::isLatest)
                .toList();
    }

    @Override
    public List<TransactionDTO> getAllTransactionsByType(String type) {
        return dbStore.stream().filter(r->!r.isDeleted()).filter(DataEntity::isLatest)
                .filter(r->type.equalsIgnoreCase(r.getType())).toList();
    }

    @Override
    public List<TransactionDTO> getAllTransactionsByCustomer(String customer) {
        return dbStore.stream().filter(r->!r.isDeleted()).filter(DataEntity::isLatest)
                .filter(r->customer.equalsIgnoreCase(r.getCounterParty1())
                        || customer.equalsIgnoreCase(r.getCounterParty2())).toList();
    }

    @Override
    public TransactionDTO insert(TransactionDTO data) {
        //TODO how to define a duplicated transaction exists already is unknown,
        // 2 customers can keep trading a product with same price and amount
        data.setId(UUID.randomUUID().toString());
        data.setTradeID(UUID.randomUUID().toString());
        data.setCreatedOn(new Date());
        data.setUpdatedOn(data.getCreatedOn());
        data.setVersion(1);
        data.setDeleted(false);
        data.setLatest(true);
        dbStore.add(data);
        transactionCacheService.addTransactionCache(data);
        return data;
    }

    @Override
    public TransactionDTO getActiveData(String tradeId) {
        return dbStore.stream().filter(r->tradeId.equals(r.getTradeID()))
                .filter(r->!r.isDeleted()).filter(DataEntity::isLatest)
                .findFirst().orElse(null);
    }

    @Override
    public List<TransactionDTO> getDataHistory(String tradeId) {
        return dbStore.stream().filter(r->tradeId.equals(r.getTradeID())).toList();
    }

    @Override
    public TransactionDTO update(TransactionDTO data) {
        Object lock = tradeIdLevelLock.computeIfAbsent(data.getTradeID(), k -> new Object());
        synchronized (lock) {
            try {
                updateInDB(data);
            } finally {
                tradeIdLevelLock.remove(data.getTradeID());
            }
        }
        return data;
    }

    private void updateInDB(TransactionDTO data) {
        TransactionDTO dataInDB = getActiveData(data.getTradeID());
        if (null == dataInDB) {
            String msg = String.format("Transaction %s doesn't exist or it's deleted", data.getTradeID());
            throw new RuntimeException(msg);
        }
        dataInDB.setLatest(false);
        data.setLatest(true);
        data.setId(UUID.randomUUID().toString());
        data.setCreatedOn(dataInDB.getCreatedOn());
        data.setUpdatedOn(new Date());
        data.setVersion(dataInDB.getVersion() + 1);
        dbStore.add(data);
        transactionCacheService.updateTransactionCache(data, dataInDB);
    }

    @Override
    public void delete(String tradeId) {
        TransactionDTO dataInDB = getActiveData(tradeId);
        if (null == dataInDB) {
            String msg = String.format("Transaction %s doesn't exist in DB or is already deleted", tradeId);
            throw new RuntimeException(msg);
        }
        dataInDB.setLatest(false);
        TransactionDTO copiedData = copy(dataInDB);
        copiedData.setId(UUID.randomUUID().toString());
        copiedData.setDeleted(true);
        copiedData.setLatest(true);
        copiedData.setVersion(dataInDB.getVersion() + 1);
        copiedData.setUpdatedOn(new Date());
        dbStore.add(copiedData);
        transactionCacheService.deleteTransactionCache(dataInDB);
    }

    private TransactionDTO copy(TransactionDTO original) {
        TransactionDTO copy = new TransactionDTO();
        copy.setId(original.getId());
        copy.setTradeID(original.getTradeID());
        copy.setType(original.getType());
        copy.setProductId(original.getProductId());
        copy.setAmount(original.getAmount());
        copy.setPrice(original.getPrice());
        copy.setDirection(original.getDirection());
        copy.setCounterParty1(original.getCounterParty1());
        copy.setCounterParty2(original.getCounterParty2());
        copy.setVersion(original.getVersion());
        copy.setCreatedOn(original.getCreatedOn());
        copy.setUpdatedOn(original.getUpdatedOn());
        copy.setDeleted(original.isDeleted());
        copy.setLatest(original.isLatest());
        return copy;
    }

    @PostConstruct
    public void init() {
        //load some mock data
        TransactionDTO historyData1 = new TransactionDTO();
        historyData1.setId("0001");
        historyData1.setTradeID("tr001");
        historyData1.setType("BOND");
        historyData1.setProductId("bond001");
        historyData1.setAmount(100L);
        historyData1.setPrice(95d);
        historyData1.setDirection("BID");
        historyData1.setCounterParty1("A");
        historyData1.setCounterParty2("B");
        historyData1.setVersion(1);
        historyData1.setCreatedOn(new Date());
        historyData1.setUpdatedOn(historyData1.getCreatedOn());
        dbStore.add(historyData1);
    }
}

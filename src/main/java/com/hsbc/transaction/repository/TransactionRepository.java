package com.hsbc.transaction.repository;

import com.hsbc.transaction.model.DataEntity;
import com.hsbc.transaction.model.TransactionDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TransactionRepository {
    //as we don't need to store data to db, write a repository class to simulate spring data

    private final List<TransactionDTO> dbStore = new ArrayList<>();

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

    public List<TransactionDTO> getAllTransactions() {
        return dbStore.stream().filter(r->!r.isDeleted()).filter(DataEntity::isLatest).toList();
    }
}

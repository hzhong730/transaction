package com.hsbc.transaction;

import com.hsbc.transaction.model.TransactionDTO;
import com.hsbc.transaction.model.vo.BondTransactionVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionDTOTestUtils {
    public static TransactionDTO aTransaction(String type, String id) {
        TransactionDTO data = new TransactionDTO();
        data.setType(type);
        data.setProductId(type + "-001");
        data.setCounterParty1("C");
        data.setCounterParty2("D");
        data.setCreatedOn(new Date());
        data.setUpdatedOn(new Date());
        data.setTradeID(id);
        data.setDirection("BID");
        data.setPrice(96d);
        data.setAmount(100L);
        return  data;
    }

    public static BondTransactionVO aTransactionVo(String type, String id) {
        TransactionDTO data = aTransaction(type, id);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return new BondTransactionVO(data, dateFormat);
    }
}

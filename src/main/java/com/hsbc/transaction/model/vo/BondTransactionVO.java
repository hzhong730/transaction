package com.hsbc.transaction.model.vo;

import com.hsbc.transaction.model.TransactionDTO;

import java.text.DateFormat;

public class BondTransactionVO {
    private final String tradeID;
    private final String counterParty1;
    private final String counterParty2;
    private final String productId;
    private final String type;
    private final String price;
    private final String amount;
    private final String direction;
    private final String createdOn;
    private final String updatedOn;
    private final String version;
    private final String deleted;

    public BondTransactionVO (TransactionDTO data, DateFormat dataFormat) {
        this.tradeID = data.getTradeID();
        this.counterParty1 = data.getCounterParty1();
        this.counterParty2 = data.getCounterParty2();
        this.productId = data.getProductId();
        this.type = data.getType();
        this.price = String.valueOf(data.getPrice());
        this.amount = String.valueOf(data.getAmount());
        this.direction = data.getDirection();
        this.createdOn = null==data.getCreatedOn()?"":dataFormat.format(data.getCreatedOn());
        this.updatedOn = null==data.getUpdatedOn()?"":dataFormat.format(data.getUpdatedOn());
        this.version = String.valueOf(data.getVersion());
        this.deleted = data.isDeleted()?"Y":"N";
    }

    public String getTradeID() {
        return tradeID;
    }

    public String getCounterParty1() {
        return counterParty1;
    }

    public String getCounterParty2() {
        return counterParty2;
    }

    public String getProductId() {
        return productId;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getAmount() {
        return amount;
    }

    public String getDirection() {
        return direction;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public String getVersion() {
        return version;
    }

    public String getDeleted() {
        return deleted;
    }
}

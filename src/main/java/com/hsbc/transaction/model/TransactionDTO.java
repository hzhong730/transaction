package com.hsbc.transaction.model;

public class TransactionDTO extends DataEntity{
    private String tradeID;
    private String counterParty1;
    private String counterParty2;
    private String productId;
    private String type;
    private Double price;
    private Long amount;
    private String direction;

    public String getTradeID() {
        return tradeID;
    }

    public void setTradeID(String tradeID) {
        this.tradeID = tradeID;
    }

    public String getCounterParty1() {
        return counterParty1;
    }

    public void setCounterParty1(String counterParty1) {
        this.counterParty1 = counterParty1;
    }

    public String getCounterParty2() {
        return counterParty2;
    }

    public void setCounterParty2(String counterParty2) {
        this.counterParty2 = counterParty2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}

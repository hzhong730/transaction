package com.hsbc.transaction.service;

import com.hsbc.transaction.model.TransactionDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidator {
    //TODO use spring validation annotation in the future to reduce code

    public List<String> validateTransactionDTO(TransactionDTO data) {
        List<String> validationResult = new ArrayList<>();
        if (data.getCounterParty1() == null || data.getCounterParty1().isBlank()) {
            validationResult.add("Counterparty 1 is empty");
        }
        if (data.getCounterParty2() == null || data.getCounterParty2().isBlank()) {
            validationResult.add("Counterparty 2 is empty");
        }
        if (data.getCounterParty1().equals(data.getCounterParty2())) {
            validationResult.add("Counterparties are same");
        }
        if (!"BOND".equals(data.getType())) {
            validationResult.add("Sorry, BOND is the only type supported now");
        }
        if (data.getProductId() == null || data.getProductId().isBlank()) {
            validationResult.add("Product ID is empty");
        }
        if (!"BID".equals(data.getDirection()) && !"ASK".equals(data.getDirection())) {
            validationResult.add("BID/ASK are directions supported");
        }
        if (data.getPrice() == null || data.getPrice() < 0d) {
            validationResult.add("Only positive price is supported");
        }
        if (data.getAmount() == null || data.getAmount() <= 0L) {
            validationResult.add("Only positive amount is supported");
        }
        return validationResult;
    }

}

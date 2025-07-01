package com.hsbc.transaction.service.validator;

import java.util.List;

public interface DataValidator<T> {
    List<String> validateTransactionDTO(T data);
}

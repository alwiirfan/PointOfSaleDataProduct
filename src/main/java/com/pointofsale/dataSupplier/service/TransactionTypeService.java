package com.pointofsale.dataSupplier.service;

import com.pointofsale.dataSupplier.constant.ETransactionType;
import com.pointofsale.dataSupplier.entity.TransactionType;

public interface TransactionTypeService {
    TransactionType getOrSave(ETransactionType transactionType);
}

package com.pointofsale.dataSupplier.service.impl;

import org.springframework.stereotype.Service;

import com.pointofsale.dataSupplier.constant.ETransactionType;
import com.pointofsale.dataSupplier.entity.TransactionType;
import com.pointofsale.dataSupplier.repository.TransactionTypeRepository;
import com.pointofsale.dataSupplier.service.TransactionTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final TransactionTypeRepository transactionTypeRepository;

    @Override
    public TransactionType getOrSave(ETransactionType transactionType) {
        // TODO set transaction type
        TransactionType type = TransactionType.builder()
                                    .transactionType(transactionType)
                                    .build();

        // TODO get or add return of transaction type
        return transactionTypeRepository.findFirstByTransactionType(transactionType)
                    .orElseGet(() -> transactionTypeRepository.save(type));
    }
    
}

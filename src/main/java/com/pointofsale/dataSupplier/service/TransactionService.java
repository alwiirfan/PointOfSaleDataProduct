package com.pointofsale.dataSupplier.service;

import org.springframework.data.domain.Page;

import com.pointofsale.dataSupplier.dto.request.NewTransactionRequest;
import com.pointofsale.dataSupplier.dto.request.SearchSalesHistoryRequest;
import com.pointofsale.dataSupplier.dto.request.SearchTransactionRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateTransactionRequest;
import com.pointofsale.dataSupplier.dto.response.TotalSales;
import com.pointofsale.dataSupplier.dto.response.TransactionResponse;

public interface TransactionService {
    TransactionResponse create(NewTransactionRequest request);
    TransactionResponse get(String transactionId);
    TransactionResponse update(UpdateTransactionRequest request, String id);
    Page<TransactionResponse> getAll(SearchTransactionRequest request, Integer page, Integer size);
    TotalSales getSalesHistory(SearchSalesHistoryRequest request);
}

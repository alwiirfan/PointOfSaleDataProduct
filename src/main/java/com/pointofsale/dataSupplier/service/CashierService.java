package com.pointofsale.dataSupplier.service;

import com.pointofsale.dataSupplier.dto.request.UpdateCashierRequest;
import com.pointofsale.dataSupplier.entity.Cashier;

public interface CashierService {
    Cashier create(Cashier request);

    // UserResponse getById(String id);
    // List<UserResponse> getAll(SearchUserRequest request);
    // UserResponse update(UpdateCashierRequest request, String id);
}

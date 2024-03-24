package com.pointofsale.dataSupplier.constant;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ETransactionType {
    CASH("Cash");

    private String name;

    public static ETransactionType getTransactionType(String value) {
        return Arrays.stream(values()).filter(eTransactionType -> eTransactionType.name.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.getNotFoundResource(ETransactionType.class)));
    }
}

package com.pointofsale.dataSupplier.constant;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum ERole {
    ROLE_SUPER_ADMIN,
    ROLE_ADMIN,
    ROLE_CASHIER;

    public static ERole getRole(String value) {
        return Arrays.stream(values()).filter(eRole -> eRole.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                         ResponseMessage.getNotFoundResource(ERole.class)));
    }
}

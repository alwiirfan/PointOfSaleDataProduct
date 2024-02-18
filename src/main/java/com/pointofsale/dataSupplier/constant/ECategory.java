package com.pointofsale.dataSupplier.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ECategory {
    MAKANAN("Makanan"),
    MINUMAN("Minuman");

    private String name;

    public static ECategory getCategory(String value) {
        return Arrays.stream(values()).filter(eCategory -> eCategory.name.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.getNotFoundResource(ECategory.class)));
    }

}

package com.pointofsale.dataSupplier.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ECategory {
    MAKANAN("Makanan"),
    HEWAN("Hewan"),
    MINUMAN("Minuman");

    private String name;

    public static ECategory getCategory(String value) {
        return Arrays.stream(values()).filter(eCategory -> eCategory.name.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.getNotFoundResource(ECategory.class)));
    }

    public static ECategory getCategoryNumber(String number) {
        try {
            return Arrays.stream(values()).filter(eCategory -> eCategory.ordinal() == Integer.parseInt(number) - 1)
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.getNotFoundResource(ECategory.class)));
        }catch (NumberFormatException e){
            log.error("Failed to parse category number: {}", number, e);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid get category");
        }
    }

}

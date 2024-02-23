package com.pointofsale.dataSupplier.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class POSUtil {

    public static LocalDateTime parseLocalDateTime(String date) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
            return localDate.atStartOfDay();
        }catch (DateTimeException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid Date Format: dd-MM-yyyy");
        }
    }

}

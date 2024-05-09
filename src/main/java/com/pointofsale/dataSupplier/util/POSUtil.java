package com.pointofsale.dataSupplier.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class POSUtil {

    // TODO parse String to LocalDateTime
    public static LocalDateTime parseLocalDateTime(String date) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
            return localDate.atStartOfDay();
        }catch (DateTimeException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid Date Format: dd-MM-yyyy");
        }
    }

    // TODO parse String to Date
    public static Date parseDate(String date) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
            return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }catch (DateTimeException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid Date Format: dd-MM-yyyy");
        }
    }

    // TODO parse LocalDateTime to LocalDate with pattern dd-MM-yyyy
    public static LocalDate parseLocalDateTimeToLocalDate(LocalDateTime localDateTime) {
        try {
            LocalDate ldt = localDateTime.toLocalDate();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(ldt.format(dateTimeFormatter), dateTimeFormatter);
            return localDate;
        }catch (DateTimeException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "local date time not valid");
        }
    }

}

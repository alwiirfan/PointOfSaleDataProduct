package com.pointofsale.dataSupplier.constant;


public class ResponseMessage {

    private static final String NOT_FOUND_RESOURCE = "%s not found";
    private static final String DUPLICATE_RESOURCE = "%s already exist";

    public static <T> String getNotFoundResource(Class<T> clazz) {
        return String.format(NOT_FOUND_RESOURCE, clazz.getSimpleName());
    }

    public static <T> String getDuplicateResource(Class<T> clazz) {
        return String.format(DUPLICATE_RESOURCE, clazz.getSimpleName());
    }
}
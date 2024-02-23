package com.pointofsale.dataSupplier.constant;


public class ResponseMessage {

    private static final String NOT_FOUND_RESOURCE = "%s not found";
    private static final String DUPLICATE_RESOURCE = "%s already exist";
    private static final String INTERNAL_SERVER_ERROR = "%s internal server error";
    private static final String BAD_REQUEST = "%s bad request";

    public static <T> String getNotFoundResource(Class<T> clazz) {
        return String.format(NOT_FOUND_RESOURCE, clazz.getSimpleName());
    }

    public static <T> String getDuplicateResource(Class<T> clazz) {
        return String.format(DUPLICATE_RESOURCE, clazz.getSimpleName());
    }

    public static <T> String getInternalServerError(Class<T> clazz) {
        return String.format(INTERNAL_SERVER_ERROR, clazz.getSimpleName());
    }

    public static <T> String getBadRequest(Class<T> clazz) {
        return String.format(BAD_REQUEST, clazz.getSimpleName());
    }
}

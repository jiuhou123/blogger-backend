package com.jiuhou.auth.utils;

public class ServiceExceptionMessageHolder {

    private static final ThreadLocal<String> MESSAGE_HOLDER = new ThreadLocal<>();

    public static void setMessage(String message) {
        MESSAGE_HOLDER.set(message);
    }

    public static String getMessage() {
        return MESSAGE_HOLDER.get();
    }

    public static void clear() {
        MESSAGE_HOLDER.remove();
    }
}
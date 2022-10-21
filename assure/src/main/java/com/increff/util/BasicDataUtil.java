package com.increff.util;

import java.text.DecimalFormat;

public class BasicDataUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static Double roundOffDouble(Double d) {
        return Double.valueOf(DECIMAL_FORMAT.format(d));
    }

    public static int length(String str) {
        return isEmpty(str) ? 0 : str.length();
    }

    public static String trimAndLowerCase(String str) {
        return str.toLowerCase().trim();
    }

}
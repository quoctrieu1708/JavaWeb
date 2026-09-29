package com.ecom.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final Locale VIETNAM = new Locale("vi", "VN");

    public static String formatVND(Double amount) {
        if (amount == null) return "0đ";
        NumberFormat formatter = NumberFormat.getInstance(VIETNAM);
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount) + "đ";
    }

    public static String formatVNDNoSymbol(Double amount) {
        if (amount == null) return "0";
        NumberFormat formatter = NumberFormat.getInstance(VIETNAM);
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }
}

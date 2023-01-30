/*
 * created by Andrew K. <rembozebest@gmail.com> on 5/14/20 5:56 AM
 * copyright (c) 2020
 * last modified 5/14/20 5:56 AM with ❤
 */

package com.add.finance.utils;

import java.text.DecimalFormat;

public class TextFormat {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public static String doubleToString(double d) {
        return decimalFormat.format(d);
    }
}

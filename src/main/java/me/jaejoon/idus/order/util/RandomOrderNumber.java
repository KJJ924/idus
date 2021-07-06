package me.jaejoon.idus.order.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */

public class RandomOrderNumber {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String digits = "0123456789";

    private final Random random = new SecureRandom();

    private final int length = 12;

    private final char[] buf = new char[length];

    public String create(String symbol) {
        String s = symbol + upper + digits;
        char[] symbols = s.toCharArray();
        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf).toUpperCase(Locale.ROOT);
    }
}

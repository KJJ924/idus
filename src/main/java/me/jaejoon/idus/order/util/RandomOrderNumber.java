package me.jaejoon.idus.order.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */

public class RandomOrderNumber {

    public static final String prefix = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String suffix = "0123456789";

    private final Random random = new SecureRandom();

    private final int length = 12;

    private final char[] buf = new char[length];

    public String create() {
        String symbol = UUID.randomUUID().toString().replaceAll("-", "");
        String s = prefix + symbol + suffix;
        char[] symbols = s.toCharArray();
        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf).toUpperCase(Locale.ROOT);
    }
}

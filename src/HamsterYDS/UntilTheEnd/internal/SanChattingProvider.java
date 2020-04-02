/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/02 01:27:02
 *
 * UntilTheEnd/UntilTheEnd/SanChattingProvider.java
 */

package HamsterYDS.UntilTheEnd.internal;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.cap.san.Sanity;
import org.jetbrains.annotations.NotNull;

import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.function.Function;
import java.util.logging.Level;

public class SanChattingProvider {
    public static final SecureRandom random = new SecureRandom();

    public static String v1(String source) {
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < Math.random() * 50; i++)
            newString.append((char) (10000 * Math.random() + 40));
        return newString.toString();
    }

    public static String OrderDisorder(String source) {
        final CharBuffer wrap = CharBuffer.wrap(source);
        StringBuilder builder = new StringBuilder();
        int insert = 0;
        boolean wok = false;
        while (wrap.hasRemaining()) {
            char next = wrap.get();
            builder.insert(insert, next);
            if (wok ^= random.nextBoolean())
                insert++;
        }
        return builder.toString();
    }

    public static String v2(String source) {
        final CharBuffer wrap = CharBuffer.wrap(source);
        StringBuilder builder = new StringBuilder();
        int insert = 0;
        boolean wok = false;
        while (wrap.hasRemaining()) {
            char next = wrap.get();
            if (random.nextBoolean()) {
                if (random.nextBoolean()) {
                    builder.append(next);
                } else {
                    builder.insert(insert, next);
                    if (wok) insert++;
                }
            } else {
                if (random.nextBoolean()) {
                    wok ^= true;
                }
                builder.insert(insert, (char) ((0xFF00 * random.nextDouble()) + 0xFF));
                if (wok) insert++;
            }
        }
        return builder.toString();
    }

    @NotNull
    public static Function<String, String> INSTANCE;

    static {
        String path = Sanity.yaml.getString("function", "v2");
        switch (path) {
            case "v1": {
                INSTANCE = SanChattingProvider::v1;
                break;
            }
            case "v2": {
                INSTANCE = SanChattingProvider::v2;
                break;
            }
            case "OrderDisorder": {
                INSTANCE = SanChattingProvider::OrderDisorder;
                break;
            }
            default: {
                Function<String, String> preSet = null;
                try {
                    Class<?> klass = Class.forName(path);
                    final Function<?, ?> function = klass.asSubclass(Function.class).getConstructor().newInstance();
                    @SuppressWarnings({"unchecked", "CastCanBeRemovedNarrowingVariableType"})
                    Function<String, String> func = (Function<String, String>) function;
                    // PreCheck
                    func.apply("Hello World");
                    preSet = func;
                } catch (Throwable fail) {
                    UntilTheEnd.getInstance().getLogger().log(Level.SEVERE,
                            "[SanityChattingProvider] Failed to provide " + path + ", " +
                                    "please check your configuration or contact provider's anchor!",
                            fail);
                }
                if (preSet == null) preSet = SanChattingProvider::v2;
                INSTANCE = preSet;
                break;
            }
        }
    }
}

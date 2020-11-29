/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 22:32:11
 *
 * UntilTheEnd/UntilTheEnd/Java9StringFactory.java
 */

package ute.internal.karlatemp.mxlib.formatter;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.CharBuffer;
import java.util.UUID;

/**
 * @see java.lang.invoke.StringConcatFactory#makeConcatWithConstants(MethodHandles.Lookup, String, MethodType, String, Object...)
 */
public class Java9StringFactory extends Formatter {
    /**
     * Tag used to demarcate an ordinary argument.
     */
    private static final char TAG_ARG = '\u0001';

    /**
     * Tag used to demarcate a constant.
     */
    private static final char TAG_CONST = '\u0002';

    public static void main(String... args) {
        System.out.println(new Java9StringFactory().parse("Hey, My \u0002 Is \u0001!",
                new ArrayReplacer("UniqueId")
        ).format(null, new ArrayReplacer(UUID.randomUUID().toString())));
    }

    @Override
    public FormatTemplate parse(@NotNull String template, @NotNull Replacer constants) {
        FormatAction.ActionLink link = new FormatAction.ActionLink();
        CharBuffer cb = CharBuffer.allocate(template.length());
        int arg = 0, cArg = 0;
        for (char c : template.toCharArray()) {
            switch (c) {
                case TAG_ARG: {
                    cb.flip();
                    if (cb.hasRemaining()) {
                        link.append(cb.toString());
                    }
                    cb.clear();
                    link.appendSlot(arg++);
                    break;
                }
                case TAG_CONST: {
                    cb.flip();
                    if (cb.hasRemaining()) {
                        link.append(cb.toString());
                    }
                    cb.clear();
                    link.append(constants.getSlot(cArg++));
                    break;
                }
                default: {
                    cb.put(c);
                    break;
                }
            }
        }
        cb.flip();
        if (cb.hasRemaining()) {
            link.append(cb.toString());
        }
        return new SimpleFormatTemplate(link);
    }
}

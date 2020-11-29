/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 22:32:11
 *
 * UntilTheEnd/UntilTheEnd/PunctuateFormatter.java
 */

package ute.internal.karlatemp.mxlib.formatter;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class PunctuateFormatter extends Formatter implements BiConsumer<StringBuilder, String> {

    private final String suf;
    private final String pre;

    public PunctuateFormatter(String prefix, String suffix) {
        if (prefix.isEmpty() || suffix.isEmpty()) {
            throw new RuntimeException("Cannot input empty string.");
        }
        this.pre = prefix;
        this.suf = suffix;
    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("A", "Karlatemp");
        map.put("B", "Bilibili");
        map.put("C", "1min");
        System.out.println(new PunctuateFormatter("$", "$").parse(
                "$A$ to $B$ used $C$.$UNKNOWN_VARIABLE$ Copyright Karlatemp.", Replacer.EMPTY
        ).format(null, new MapMapping<>(map)));
    }

    @Override
    public FormatTemplate parse(@NotNull String template, @NotNull Replacer constants) {
        FormatAction.ActionLink link = new FormatAction.ActionLink();
        final int size = template.length();
        int off = 0;
        do {
            final int ind = template.indexOf(pre, off);
            if (ind == -1) {
                link.append(template.substring(off));
                break;
            }
            link.append(template.substring(off, ind));
            off = ind + pre.length();
            int ending = template.indexOf(suf, off);
            if (ending == -1) {
                link.append(template.substring(off));
                break;
            } else {
                String key = template.substring(off, ending);
                link.appendKey(key, this);
                off = ending + suf.length();
            }
        } while (off < size);

        return new SimpleFormatTemplate(link);
    }

    @Override
    public void accept(StringBuilder builder, String s) {
        builder.append(pre).append(s).append(suf);
    }
}

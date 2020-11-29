/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 22:32:11
 *
 * UntilTheEnd/UntilTheEnd/ArrayReplacer.java
 */

package ute.internal.karlatemp.mxlib.formatter;

import java.util.Arrays;
import java.util.List;

public class ArrayReplacer implements Replacer {
    private final List<String> list;

    public ArrayReplacer(List<String> list) {
        this.list = list;
    }

    public ArrayReplacer(String... list) {
        this(Arrays.asList(list));
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public boolean containsSlot(int slot) {
        return slot >= 0 && slot < list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String apply(String s) {
        return null;
    }

    @Override
    public void apply(StringBuilder builder, String key) {

    }

    @Override
    public void apply(StringBuilder builder, int slot) {
        if (containsSlot(slot))
            builder.append(list.get(slot));
    }

    @Override
    public String getKey(String key) {
        return null;
    }

    @Override
    public String getSlot(int slot) {
        StringBuilder sb = new StringBuilder();
        apply(sb, slot);
        return sb.toString();
    }
}

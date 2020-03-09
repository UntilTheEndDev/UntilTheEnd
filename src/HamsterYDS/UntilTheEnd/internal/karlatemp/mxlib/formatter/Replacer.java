/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 22:32:11
 *
 * UntilTheEnd/UntilTheEnd/Replacer.java
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HamsterYDS.UntilTheEnd.internal.karlatemp.mxlib.formatter;

import java.util.function.Function;

/**
 * @author 32798
 */
public interface Replacer extends Function<String, String> {

    boolean containsKey(String key);

    default boolean containsSlot(int slot) {
        return containsKey(String.valueOf(slot));
    }

    boolean isEmpty();

    @Override
    default String apply(String s) {
        if (!containsKey(s)) return null;
        StringBuilder sb = new StringBuilder();
        apply(sb, s);
        return sb.toString();
    }

    void apply(StringBuilder builder, String key);

    void apply(StringBuilder builder, int slot);

    default String getKey(String key) {
        return apply(key);
    }

    default String getSlot(int slot) {
        if (!containsSlot(slot)) return null;
        StringBuilder sb = new StringBuilder();
        apply(sb, slot);
        return sb.toString();
    }

    default Object magicValue() {
        return null;
    }

    Replacer EMPTY = new Replacer() {
        @Override
        public boolean containsKey(String key) {
            return false;
        }

        @Override
        public boolean containsSlot(int slot) {
            return false;
        }

        @Override
        public String getSlot(int slot) {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String apply(String s) {
            return null;
        }

        @Override
        public void apply(StringBuilder builder, String key) {
        }

        @Override
        public void apply(StringBuilder builder, int key) {
        }

        @Override
        public String getKey(String key) {
            return null;
        }
    };
}

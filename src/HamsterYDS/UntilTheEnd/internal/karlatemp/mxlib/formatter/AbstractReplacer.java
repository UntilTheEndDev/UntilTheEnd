/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 22:32:11
 *
 * UntilTheEnd/UntilTheEnd/AbstractReplacer.java
 */

package HamsterYDS.UntilTheEnd.internal.karlatemp.mxlib.formatter;

public interface AbstractReplacer extends Replacer {
    @Override
    default boolean containsKey(String key) {
        return true;
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default void apply(StringBuilder builder, String key) {
        builder.append(key);
    }

    @Override
    default void apply(StringBuilder builder, int slot) {
        apply(builder, String.valueOf(slot));
    }
}

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 22:32:11
 *
 * UntilTheEnd/UntilTheEnd/SimpleFormatTemplate.java
 */

package ute.internal.karlatemp.mxlib.formatter;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class SimpleFormatTemplate implements FormatTemplate {
    private final FormatAction action;

    public SimpleFormatTemplate(FormatAction action) {
        this.action = action;
    }

    @Override
    public String format(Locale locale, @NotNull Replacer replacer) {
        return action.get(locale, replacer);
    }

    public FormatAction getAction() {
        return action;
    }
}

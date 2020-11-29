/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 22:32:11
 *
 * UntilTheEnd/UntilTheEnd/FormatAction.java
 */

package ute.internal.karlatemp.mxlib.formatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;

public abstract class FormatAction {
    protected boolean cache = false;
    protected int lastSize = 16;

    public boolean sizeCache() {
        return cache;
    }

    public int builderSize() {
        return lastSize;
    }

    public FormatAction builderSize(int size) {
        lastSize = size;
        return this;
    }

    public FormatAction sizeCache(boolean cache) {
        this.cache = cache;
        return this;
    }

    public String get(Locale locale, Replacer replacer) {
        StringBuilder builder = new StringBuilder(lastSize);
        get(builder, locale, replacer);
        String result = builder.toString();
        if (cache) lastSize = result.length();
        return result;
    }

    public abstract void get(@NotNull StringBuilder builder, Locale locale, Replacer replacer);

    public FormatAction addThen(FormatAction next) {
        if (next == null) return this;
        return new ActionLink().addThen(this).addThen(next);
    }

    public static class ActionLink extends FormatAction {
        protected final Collection<FormatAction> actions;
        protected boolean closed;

        public Collection<FormatAction> getActions() {
            return actions;
        }

        public ActionLink() {
            this(new ConcurrentLinkedQueue<>());
        }

        @Override
        public ActionLink sizeCache(boolean cache) {
            super.sizeCache(cache);
            return this;
        }

        public ActionLink close() {
            closed = true;
            return this;
        }

        @Override
        public ActionLink builderSize(int size) {
            super.builderSize(size);
            return this;
        }

        public ActionLink(Collection<FormatAction> actions) {
            this.actions = actions;
        }

        @Override
        public void get(@NotNull StringBuilder builder, Locale locale, Replacer replacer) {
            for (FormatAction action : actions) {
                action.get(builder, locale, replacer);
            }
        }

        public ActionLink clear() {
            if (closed) return new ActionLink();
            actions.clear();
            return this;
        }

        public int actions() {
            int size = 0;
            for (FormatAction action : actions) {
                if (action instanceof ActionLink) {
                    size += ((ActionLink) action).actions();
                } else {
                    size++;
                }
            }
            return size;
        }

        @Override
        public ActionLink addThen(FormatAction next) {
            if (next == null) {
                if (closed)
                    return new ActionLink().addThen(this);
                return this;
            }
            if (closed) {
                return new ActionLink().addThen(this).addThen(next);
            }
            actions.add(next);
            return this;
        }

        public ActionLink append(CharSequence str) {
            if (str == null || str.length() == 0) {
                return addThen(null);
            }
            return addThen(new ActionStaticString(str, 0, str.length()));
        }

        public ActionLink append(CharSequence str, int offset, int len) {
            if (str == null || len < 1) return addThen(null);
            return addThen(new ActionStaticString(str, offset, len));
        }

        public ActionLink append(char[] str) {
            if (str == null || str.length == 0) return addThen(null);
            return addThen(new ActionStaticChars(str, 0, str.length));
        }

        public ActionLink append(char[] str, int offset, int len) {
            if (str == null || len == 0) return addThen(null);
            return addThen(new ActionStaticChars(str, offset, len));
        }

        public ActionLink appendKey(String key) {
            return addThen(new ActionGetKey(key));
        }

        public ActionLink appendKey(String key, BiConsumer<StringBuilder, String> unknown_variable) {
            return addThen(new ActionGetKey(key, unknown_variable));
        }

        public ActionLink appendSlot(int slot, ObjIntConsumer<StringBuilder> unknown_variable) {
            return addThen(new ActionGetSlot(slot, unknown_variable));
        }

        public ActionLink appendSlot(int slot) {
            return addThen(new ActionGetSlot(slot));
        }
    }

    public static class ActionGetKey extends FormatAction {
        private final String key;
        private final BiConsumer<StringBuilder, String> unknown_variable;

        public String getKey() {
            return key;
        }

        public ActionGetKey(@NotNull String key) {
            this(key, null);
        }

        public ActionGetKey(@NotNull String key, @Nullable BiConsumer<StringBuilder, String> unknown_variable) {
            this.key = key;
            this.unknown_variable = unknown_variable;
        }

        @Override
        public void get(@NotNull StringBuilder builder, Locale locale, Replacer replacer) {
            Objects.requireNonNull(replacer, "The mapping cannot be null.");
            if (replacer.isEmpty()) return;
            if (replacer.containsKey(key)) {
                replacer.apply(builder, key);
            } else if (unknown_variable != null) {
                unknown_variable.accept(builder, key);
            }
        }
    }

    public static class ActionGetSlot extends FormatAction {
        private final int slot;
        private final ObjIntConsumer<StringBuilder> unknown_variable;

        public ActionGetSlot(int slot) {
            this(slot, null);
        }

        public ActionGetSlot(int slot, ObjIntConsumer<StringBuilder> unknown_variable) {
            this.slot = slot;
            this.unknown_variable = unknown_variable;
        }

        @Override
        public void get(@NotNull StringBuilder builder, Locale locale, Replacer replacer) {
            Objects.requireNonNull(replacer, "The mapping cannot be null.");
            if (replacer.isEmpty()) return;
            if (replacer.containsSlot(slot)) {
                replacer.apply(builder, slot);
            } else if (unknown_variable != null) {
                unknown_variable.accept(builder, slot);
            }
        }
    }

    public static class ActionStaticString extends FormatAction {
        private final int offset;
        private final CharSequence str;
        private final int len;

        public ActionStaticString(CharSequence str, int offset, int len) {
            this.str = str;
            this.offset = offset;
            this.len = len;
        }

        @Override
        public void get(@NotNull StringBuilder builder, Locale locale, Replacer replacer) {
            builder.append(str, offset, len);
        }
    }

    public static class ActionStaticChars extends FormatAction {
        private final int offset;
        private final char[] str;
        private final int len;

        public ActionStaticChars(char[] str, int offset, int len) {
            this.str = str;
            this.offset = offset;
            this.len = len;
        }

        @Override
        public void get(@NotNull StringBuilder builder, Locale locale, Replacer replacer) {
            builder.append(str, offset, len);
        }
    }
}

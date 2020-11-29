/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/02 09:37:54
 *
 * UntilTheEnd/UntilTheEnd/NativeFunction.java
 */

package ute.internal;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.ECMAException;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class NativeFunction implements JSObject {
    protected String name;

    @Override
    public Object call(Object o, Object... objects) {
        Object result;
        try {
            result = call0(o, objects);
        } catch (Throwable throwable) {
            if (throwable instanceof ExceptionBox) {
                throw ECMAException.create(((ExceptionBox) throwable).exception, getClassName() + ".native", 4, 4);
            }
            throw ECMAException.create(throwable, getClassName() + ".native", 4, 4);
        }
        if (result instanceof ExceptionBox) {
            throw ECMAException.create(((ExceptionBox) result).exception, getClassName() + ".native", 4, 4);
        }
        return result;
    }

    protected abstract Object call0(Object o, Object... objects) throws Throwable;

    private static final NativeFunction TO_STRING_TO_STRING = new NativeFunction((Void) null) {
        {
            this.TO_STRING = this;
        }

        @Override
        protected Object call0(Object o, Object... objects) throws Throwable {
            return call(o, objects);
        }

        @Override
        public Object call(Object o, Object... objects) {
            return "function toString(){ [native code] }";
        }
    };

    NativeFunction(Void ignored) {
        TO_STRING = TO_STRING_TO_STRING;
    }

    protected NativeFunction() {
        this((String) null);
    }

    protected NativeFunction(String name) {
        if (name == null) name = "NativeMethod";
        TO_STRING = new NativeFunction((Void) null) {
            NativeFunction thiz;

            NativeFunction init(NativeFunction thiz) {
                this.thiz = thiz;
                name = "toString";
                return this;
            }

            @Override
            protected Object call0(Object o, Object... objects) throws Throwable {
                return call(o, objects);
            }

            public String toString() {
                return "function toString(){ [native code] }";
            }

            @Override
            public Object call(Object o, Object... objects) {
                return thiz.toString();
            }
        }.init(this);
        this.name = name;
    }

    NativeFunction TO_STRING;

    @Override
    public Object newObject(Object... objects) {
        throw new UnsupportedOperationException("No newInstace yet.");
    }

    @Override
    public Object eval(String s) {
        throw new UnsupportedOperationException("No EvalContext Here");
    }

    @Override
    public Object getMember(String s) {
        if ("toString".equals(s)) {
            return TO_STRING;
        }
        return null;
    }

    @Override
    public Object getSlot(int i) {
        return null;
    }

    @Override
    public boolean hasMember(String s) {
        return false;
    }

    @Override
    public boolean hasSlot(int i) {
        return false;
    }

    @Override
    public void removeMember(String s) {
    }

    @Override
    public void setMember(String s, Object o) {
    }

    @Override
    public void setSlot(int i, Object o) {
    }

    @Override
    public Set<String> keySet() {
        return Collections.emptySet();
    }

    @Override
    public Collection<Object> values() {
        return Collections.emptySet();
    }

    @Override
    public boolean isInstance(Object o) {
        return false;
    }

    @Override
    public boolean isInstanceOf(Object o) {
        return false;
    }

    @Override
    public String getClassName() {
        return name;
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public boolean isStrictFunction() {
        return true;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public double toNumber() {
        return 0;
    }

    @Override
    public String toString() {
        return "function " + getClassName() + "(){ [native code] }";
    }
}


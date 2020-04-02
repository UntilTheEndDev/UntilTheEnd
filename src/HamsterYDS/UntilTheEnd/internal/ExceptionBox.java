/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/02 09:38:35
 *
 * UntilTheEnd/UntilTheEnd/ExceptionBox.java
 */

package HamsterYDS.UntilTheEnd.internal;

public class ExceptionBox extends Throwable {
    public final Object exception;

    public ExceptionBox(Object exception) {
        super(null, null, false, false);
        this.exception = exception;
    }
}
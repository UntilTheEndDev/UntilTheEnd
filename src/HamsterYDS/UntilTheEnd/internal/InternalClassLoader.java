/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/11 19:47:39
 *
 * UntilTheEnd/UntilTheEnd/InternalClassLoader.java
 */

package HamsterYDS.UntilTheEnd.internal;

import java.util.Base64;

public class InternalClassLoader extends ClassLoader {
    private InternalClassLoader() {
        super(InternalClassLoader.class.getClassLoader());
    }

    public static final InternalClassLoader loader = new InternalClassLoader();

    public Class<?> load(byte[] data) {
        return defineClass(null, data, 0, data.length);
    }

    public Class<?> load(String encoded) {
        return load(Base64.getDecoder().decode(encoded));
    }
}

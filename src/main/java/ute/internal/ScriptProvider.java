/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/02 09:31:39
 *
 * UntilTheEnd/UntilTheEnd/ScriptProvider.java
 */

package ute.internal;

import ute.Logging;
import ute.UntilTheEnd;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.internal.runtime.Undefined;

import javax.script.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class ScriptProvider {
    public static final NashornScriptEngine ENGINE = (NashornScriptEngine) new NashornScriptEngineFactory()
            .getScriptEngine(new String[]{"--language=es6"}, ScriptProvider.class.getClassLoader());
    private final CompiledScript script;
    private final Bindings global;
    private String name = "Script@" + hashCode();
    public static final Map<String, CompiledScript> storedScripts = new ConcurrentHashMap<>();

    public ScriptProvider(CompiledScript script) {
        this.script = script;
        this.global = script.getEngine().createBindings();
    }

    public ScriptProvider name(String name) {
        this.name = name;
        return this;
    }

    public ScriptProvider append(String name, Object value) {
        global.put(name, value);
        return this;
    }

    public Object invoke() {
        AtomicReference<Object> result = new AtomicReference<>();
        global.put("result", new NativeFunction("Result") {
            @Override
            protected Object call0(Object o, Object... objects) throws Throwable {
                result.set(objects[0]);
                return Undefined.getUndefined();
            }
        });
        ScriptContext context = new SimpleScriptContext();
        context.setBindings(global, ScriptContext.ENGINE_SCOPE);
        context.setAttribute(ScriptEngine.FILENAME, name, ScriptContext.ENGINE_SCOPE);
        Object o;
        try {
            o = script.eval(context);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        if (o == null || o instanceof Undefined) {
            return result.get();
        }
        return o;
    }

    public static CompiledScript compile(Reader script) {
        try {
            return ENGINE.compile(script);
        } catch (ScriptException ex) {
            throw new RuntimeException("Failed to compile script.", ex);
        }
    }

    public static ScriptProvider of(CompiledScript script) {
        return new ScriptProvider(script);
    }

    public static CompiledScript of(String script) {
        final CompiledScript stored = storedScripts.get(script);
        if (stored != null) return stored;
        File sf = new File(UntilTheEnd.getInstance().getDataFolder(), "scripts/" + script);
        if (sf.isFile()) {
            try (Reader reader = new InputStreamReader(new FileInputStream(sf), StandardCharsets.UTF_8)) {
                final CompiledScript compile = ScriptProvider.compile(reader);
                storedScripts.put(script, compile);
                return compile;
            } catch (Exception e) {
                Logging.getLogger().log(Level.SEVERE, "Failed to load script " + script, e);
            }
        }
        return null;
    }
}

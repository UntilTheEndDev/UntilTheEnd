package HamsterYDS.UntilTheEnd.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ReflectionUtil {
    /*
     * The server version string to location NMS & OBC classes
     */
    private static String versionString;

    /*
     * Cache of NMS classes that we've searched for
     */
    private static final Map<String, Class<?>> loadedNMSClasses = new HashMap<String, Class<?>>();

    /*
     * Cache of OBS classes that we've searched for
     */
    private static final Map<String, Class<?>> loadedOBCClasses = new HashMap<String, Class<?>>();

    /*
     * Cache of methods that we've found in particular classes
     */
    private static final Map<Class<?>, Map<String, Method>> loadedMethods = new HashMap<Class<?>, Map<String, Method>>();

    /*
     * Cache of fields that we've found in particular classes
     */
    private static final Map<Class<?>, Map<String, Field>> loadedFields = new HashMap<Class<?>, Map<String, Field>>();
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final MethodType type_Function = MethodType.methodType(Function.class);
    private static final MethodType type_Function_apply = MethodType.methodType(Object.class, Object.class);

    @SuppressWarnings({"unused"})
    public static <T, R> Function<T, R> bindTo(Class<R> result, Method method) {
        try {
            final MethodHandle handle = lookup.unreflect(method);
            return bindMethodHandle(result, handle, lookup);
        } catch (Error | RuntimeException re) {
            throw re;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public interface UncheckedCode<T> {
        T call(Object... args) throws Throwable;
    }

    public static <T> T runUncheck(UncheckedCode<T> callable, Object... args) {
        try {
            return callable.call(args);
        } catch (Error | RuntimeException re) {
            throw re;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <T, R> Function<T, R> bindTo(Class<R> result, Field field) {
        try {
            final MethodHandle handle = lookup.unreflectGetter(field);
            return bindMethodHandle(result, handle, lookup);
        } catch (Error | RuntimeException re) {
            throw re;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, R> Function<T, R> bindMethodHandle(Class<R> result, MethodHandle handle, MethodHandles.Lookup lookup) {
        try {
            return (Function<T, R>) LambdaMetafactory.metafactory(lookup, "apply",
                    type_Function,
                    type_Function_apply, handle, MethodType.methodType(result, handle.type().parameterType(0))).getTarget().invoke();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Gets the version string for NMS & OBC class paths
     *
     * @return The version string of OBC and NMS packages
     */
    public static String getVersion() {
        if (versionString == null) {
            String name = Bukkit.getServer().getClass().getPackage().getName();
            versionString = name.substring(name.lastIndexOf('.') + 1) + ".";
        }

        return versionString;
    }

    /**
     * Get an NMS Class
     *
     * @param nmsClassName The name of the class
     * @return The class
     */
    public static Class<?> getNMSClass(String nmsClassName) {
        if (loadedNMSClasses.containsKey(nmsClassName)) {
            return loadedNMSClasses.get(nmsClassName);
        }

        String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            return loadedNMSClasses.put(nmsClassName, null);
        }

        loadedNMSClasses.put(nmsClassName, clazz);
        return clazz;
    }

    /**
     * Get a class from the org.bukkit.craftbukkit package
     *
     * @param obcClassName the path to the class
     * @return the found class at the specified path
     */
    public synchronized static Class<?> getOBCClass(String obcClassName) {
        if (loadedOBCClasses.containsKey(obcClassName)) {
            return loadedOBCClasses.get(obcClassName);
        }

        String clazzName = "org.bukkit.craftbukkit." + getVersion() + obcClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            loadedOBCClasses.put(obcClassName, null);
            return null;
        }

        loadedOBCClasses.put(obcClassName, clazz);
        return clazz;
    }

    /**
     * Get a Bukkit {@link Player} players NMS playerConnection object
     *
     * @param player The player
     * @return The players connection
     */
    public static Object getConnection(Player player) {
        Method getHandleMethod = getMethod(player.getClass(), "getHandle");

        if (getHandleMethod != null) {
            try {
                Object nmsPlayer = getHandleMethod.invoke(player);
                Field playerConField = getField(nmsPlayer.getClass(), "playerConnection");
                return playerConField.get(nmsPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Get a classes constructor
     *
     * @param clazz  The constructor class
     * @param params The parameters in the constructor
     * @return The constructor object
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Get a method from a class that has the specific paramaters
     *
     * @param clazz      The class we are searching
     * @param methodName The name of the method
     * @param params     Any parameters that the method has
     * @return The method with appropriate paramaters
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        if (!loadedMethods.containsKey(clazz)) {
            loadedMethods.put(clazz, new HashMap<>());
        }

        Map<String, Method> methods = loadedMethods.get(clazz);

        if (methods.containsKey(methodName)) {
            return methods.get(methodName);
        }

        try {
            Method method = clazz.getMethod(methodName, params);
            methods.put(methodName, method);
            loadedMethods.put(clazz, methods);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
            methods.put(methodName, null);
            loadedMethods.put(clazz, methods);
            throw (Error) new NoSuchMethodError("Method not found").initCause(e);
        }
    }

    /**
     * Get a field with a particular name from a class
     *
     * @param clazz     The class
     * @param fieldName The name of the field
     * @return The field object
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        if (!loadedFields.containsKey(clazz)) {
            loadedFields.put(clazz, new HashMap<String, Field>());
        }

        Map<String, Field> fields = loadedFields.get(clazz);

        if (fields.containsKey(fieldName)) {
            return fields.get(fieldName);
        }

        try {
            Field field = clazz.getField(fieldName);
            fields.put(fieldName, field);
            loadedFields.put(clazz, fields);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            fields.put(fieldName, null);
            loadedFields.put(clazz, fields);
            throw (Error) new NoSuchFieldError("Field not found").initCause(e);
        }
    }

    public static Method findMethod(Class<?> klass,
                                    int requires,
                                    int excludes,
                                    Class<?> result,
                                    Class<?>... params) {
        int paramsCount = params.length;
        for (Method m : klass.getMethods()) {
            if (m.getParameterCount() == paramsCount) {
                if (m.getReturnType() == result) {
                    int modifier = m.getModifiers();
                    if ((modifier & requires) == requires && (modifier & excludes) == 0) {
                        if (Arrays.equals(m.getParameterTypes(), params)) {
                            return m;
                        }
                    }
                }
            }
        }
        throw new NoSuchMethodError();
    }
}

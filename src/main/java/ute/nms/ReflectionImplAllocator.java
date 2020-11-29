package ute.nms;

public class ReflectionImplAllocator {
    static final String package_name = ReflectionImplAllocator.class.getPackage().getName() + '.';

    public static <T> T allocate(String name, Class<T> type) {
        try {
            return Class.forName(package_name + ReflectionUtil.getVersion() + name)
                    .asSubclass(type).newInstance();
        } catch (Throwable r0) {
            try {
                return Class.forName(package_name + "reflect." + name)
                        .asSubclass(type).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                RuntimeException re = new RuntimeException(e);
                re.addSuppressed(r0);
                throw re;
            }
        }
    }
}

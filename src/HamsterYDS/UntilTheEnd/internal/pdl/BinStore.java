package HamsterYDS.UntilTheEnd.internal.pdl;

import HamsterYDS.UntilTheEnd.api.spi.PlayerDataLoader;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Create at 2020/3/8 0:12
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.internal.pdl
 */
public abstract class BinStore implements PlayerDataLoader {
    public enum Types {
        STRING,
        MAP,
        LIST,
        BYTE,
        SHORT,
        FLOAT,
        LONG,
        DOUBLE,
        BOOLEAN,
        INTEGER,
        CHAR,
        BYTE_ARRAY,
        NULL
    }

    protected abstract File openFile(File playerData, OfflinePlayer player);

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> load(File playerdata, OfflinePlayer player) throws IOException {
        final File file = openFile(playerdata, player);
        if (!file.isFile()) return null;
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            return (Map<String, Object>) load(raf);
        }
    }

    protected void save(Object object, RandomAccessFile raf) throws IOException {
        if (object == null) {
            raf.write(Types.NULL.ordinal());
        } else if (object instanceof String) {
            raf.write(Types.STRING.ordinal());
            raf.writeUTF(object.toString());
        } else if (object instanceof Map) {
            raf.write(Types.MAP.ordinal());
            raf.writeShort(((Map<?, ?>) object).size());
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
                raf.writeUTF(String.valueOf(entry.getKey()));
                save(entry.getValue(), raf);
            }
        } else if (object instanceof List) {
            raf.write(Types.LIST.ordinal());
            raf.writeShort(((List<?>) object).size());
            for (Object o : (List<?>) object) {
                save(o, raf);
            }
        } else if (object instanceof Long) {
            raf.write(Types.LONG.ordinal());
            raf.writeLong((Long) object);
        } else if (object instanceof Boolean) {
            raf.writeLong(Types.BOOLEAN.ordinal());
            raf.writeBoolean((Boolean) object);
        } else if (object instanceof Byte) {
            raf.write(Types.BYTE.ordinal());
            raf.write((Byte) object);
        } else if (object instanceof Character) {
            raf.write(Types.CHAR.ordinal());
            raf.writeChar((Character) object);
        } else if (object instanceof Float) {
            raf.write(Types.FLOAT.ordinal());
            raf.writeFloat((Float) object);
        } else if (object instanceof Short) {
            raf.write(Types.SHORT.ordinal());
            raf.writeShort((Short) object);
        } else if (object instanceof Double) {
            raf.write(Types.DOUBLE.ordinal());
            raf.writeDouble((Double) object);
        } else if (object instanceof Integer) {
            raf.write(Types.INTEGER.ordinal());
            raf.writeInt((Integer) object);
        } else if (object instanceof byte[]) {
            raf.write(Types.BYTE_ARRAY.ordinal());
            byte[] a = (byte[]) object;
            raf.writeInt(a.length);
            raf.write(a);
        } else {
            throw new IOException(object.getClass().toString());
        }
    }

    protected Object load(RandomAccessFile raf) throws IOException {
        int size;
        switch (Types.values()[raf.readUnsignedByte()]) {
            case NULL:
                return null;
            case STRING:
                return raf.readUTF();
            case MAP:
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                size = raf.readUnsignedShort();
                while (size-- > 0) {
                    map.put(raf.readUTF(), load(raf));
                }
                return map;
            case LIST:
                ArrayList<Object> list = new ArrayList<>();
                size = raf.readUnsignedShort();
                while (size-- > 0) {
                    list.add(load(raf));
                }
                return list;
            case LONG:
                return raf.readLong();
            case BOOLEAN:
                return raf.readBoolean();
            case BYTE:
                return raf.readByte();
            case CHAR:
                return raf.readChar();
            case FLOAT:
                return raf.readFloat();
            case SHORT:
                return raf.readShort();
            case DOUBLE:
                return raf.readDouble();
            case INTEGER:
                return raf.readInt();
            case BYTE_ARRAY:
                size = raf.readInt();
                byte[] array = new byte[size];
                raf.readFully(array);
                return array;
        }
        throw new IOException();
    }

    @Override
    public void save(File playerdata, OfflinePlayer player, Map<String, Object> data) throws IOException {
        playerdata.mkdirs();
        try (RandomAccessFile raf = new RandomAccessFile(openFile(playerdata, player), "rw")) {
            save(data, raf);
        }
    }
}

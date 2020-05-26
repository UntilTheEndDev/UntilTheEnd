/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/26 21:43:06
 *
 * until-the-end/until-the-end.main/WChunk.java
 */

package HamsterYDS.UntilTheEnd.api;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.Serializable;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.UUID;

public class WChunk implements Serializable {
    private static final long serialVersionUID = 17849812312414154L;
    public String name;
    public UUID uuid;
    public int x, z;

    public WChunk(UUID uuid, String name, int x, int z) {
        this(name, uuid, x, z);
    }

    public WChunk(String name, UUID uuid, int x, int z) {
        this.name = name;
        this.uuid = uuid;
        this.x = x;
        this.z = z;
    }

    public WChunk(String name, int x, int z) {
        this(name, null, x, z);
    }

    public WChunk(UUID uuid, int x, int z) {
        this(null, uuid, x, z);
    }

    public Chunk toChunk() {
        assertValid();
        World w;
        if (uuid != null) {
            w = Bukkit.getWorld(uuid);
            if (name != null) {
                if (!w.getName().equals(name)) {
                    throw new NoSuchElementException("World `" + uuid + "` loaded with " + w + ", but name not match. except " + name + " but got " + w.getName());
                }
            }
        } else {
            w = Bukkit.getWorld(name);
        }
        w.loadChunk(x, z);
        return w.getChunkAt(x, z);
    }

    public WChunk assertValid() {
        if (name == null && uuid == null) {
            throw new NoSuchElementException("World not preset.");
        }
        return this;
    }

    @SuppressWarnings("UnstableApiUsage")
    public String serialize() {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput(Integer.SIZE * 2);
        if (name == null) {
            output.writeBoolean(false);
        } else {
            output.writeBoolean(true);
            output.writeUTF(name);
        }
        if (uuid == null) {
            output.writeBoolean(false);
        } else {
            output.writeBoolean(true);
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
        }
        output.writeInt(x);
        output.writeInt(z);
        return Base64.getEncoder().encodeToString(output.toByteArray());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static WChunk from(String data) {
        final ByteArrayDataInput decoded = ByteStreams.newDataInput(Base64.getDecoder().decode(data));
        String name;
        UUID uuid;
        if (decoded.readBoolean()) {
            name = decoded.readUTF();
        } else {
            name = null;
        }
        if (decoded.readBoolean()) {
            uuid = new UUID(decoded.readLong(), decoded.readLong());
        } else uuid = null;
        return new WChunk(name, uuid, decoded.readInt(), decoded.readInt());
    }

    public static WChunk from(Chunk chunk) {
        return from(chunk, true, false);
    }

    public static WChunk from(Chunk chunk, boolean hasUUID, boolean hasName) {
        return new WChunk(
                hasUUID ? chunk.getWorld().getUID() : null,
                hasName ? chunk.getWorld().getName() : null,
                chunk.getX(),
                chunk.getZ()
        );
    }
}

package jp.rtm.addonpackchecker.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import jp.rtm.addonpackchecker.PackEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ManifestMessage implements IMessage {
    private List<PackEntry> entries = Collections.emptyList();
    private String scanError;

    public ManifestMessage() {
    }

    public ManifestMessage(List<PackEntry> entries, String scanError) {
        this.entries = entries;
        this.scanError = scanError;
    }

    public List<PackEntry> getEntries() {
        return entries;
    }

    public String getScanError() {
        return scanError;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        boolean hasError = buffer.readBoolean();
        scanError = hasError ? ByteBufUtils.readUTF8String(buffer) : null;
        int count = buffer.readUnsignedShort();
        if (count > 10000) {
            throw new IllegalArgumentException("Too many RTM addon pack entries: " + count);
        }
        List<PackEntry> decoded = new ArrayList<PackEntry>(count);
        for (int i = 0; i < count; i++) {
            String path = ByteBufUtils.readUTF8String(buffer);
            long size = buffer.readLong();
            long lastModified = buffer.readLong();
            String sha256 = ByteBufUtils.readUTF8String(buffer);
            decoded.add(new PackEntry(path, size, lastModified, sha256));
        }
        entries = decoded;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeBoolean(scanError != null);
        if (scanError != null) {
            ByteBufUtils.writeUTF8String(buffer, scanError);
        }
        buffer.writeShort(entries.size());
        for (PackEntry entry : entries) {
            ByteBufUtils.writeUTF8String(buffer, entry.getPath());
            buffer.writeLong(entry.getSize());
            buffer.writeLong(entry.getLastModified());
            ByteBufUtils.writeUTF8String(buffer, entry.getSha256());
        }
    }
}

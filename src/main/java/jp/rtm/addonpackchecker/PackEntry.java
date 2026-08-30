package jp.rtm.addonpackchecker;

import java.util.Locale;

public final class PackEntry implements Comparable<PackEntry> {
    private final String path;
    private final long size;
    private final long lastModified;
    private final String sha256;

    public PackEntry(String path, long size, long lastModified, String sha256) {
        this.path = path.replace('\\', '/');
        this.size = size;
        this.lastModified = lastModified;
        this.sha256 = sha256.toLowerCase(Locale.ROOT);
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getSha256() {
        return sha256;
    }

    public String shortHash() {
        return sha256.length() <= 12 ? sha256 : sha256.substring(0, 12);
    }

    @Override
    public int compareTo(PackEntry other) {
        return this.path.compareTo(other.path);
    }
}

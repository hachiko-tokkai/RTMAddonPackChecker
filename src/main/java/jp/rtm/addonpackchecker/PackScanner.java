package jp.rtm.addonpackchecker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public final class PackScanner {
    private PackScanner() {
    }

    public static List<PackEntry> scan(File modsDirectory, int maximumPacks) throws IOException {
        List<File> archives = new ArrayList<File>();
        collectArchives(modsDirectory, archives);

        List<PackEntry> result = new ArrayList<PackEntry>();
        for (File archive : archives) {
            if (containsRtmModelConfig(archive)) {
                if (result.size() >= maximumPacks) {
                    throw new IOException("Detected more than " + maximumPacks + " RTM addon packs");
                }
                result.add(new PackEntry(
                        relativePath(modsDirectory, archive),
                        archive.length(),
                        archive.lastModified(),
                        sha256(archive)
                ));
            }
        }
        Collections.sort(result);
        return result;
    }

    private static void collectArchives(File directory, List<File> output) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                collectArchives(file, output);
            } else {
                String lowerName = file.getName().toLowerCase(Locale.ROOT);
                if (lowerName.endsWith(".zip") || lowerName.endsWith(".jar")) {
                    output.add(file);
                }
            }
        }
    }

    private static boolean containsRtmModelConfig(File archive) throws IOException {
        ZipFile zip = null;
        try {
            zip = openZip(archive);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    String name = new File(entry.getName()).getName();
                    if (name.startsWith("Model") && name.endsWith(".json")) {
                        return true;
                    }
                }
            }
            return false;
        } catch (ZipException invalidArchive) {
            return false;
        } finally {
            if (zip != null) {
                zip.close();
            }
        }
    }

    private static ZipFile openZip(File archive) throws IOException {
        try {
            return new ZipFile(archive, Charset.forName("UTF-8"));
        } catch (IllegalArgumentException utf8NameError) {
            return new ZipFile(archive, Charset.forName("MS932"));
        }
    }

    private static String relativePath(File root, File file) {
        try {
            return root.toURI().relativize(file.toURI()).getPath();
        } catch (RuntimeException ignored) {
            return file.getName();
        }
    }

    private static String sha256(File file) throws IOException {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 is unavailable", impossible);
        }

        InputStream input = new BufferedInputStream(new FileInputStream(file));
        try {
            byte[] buffer = new byte[64 * 1024];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                if (read > 0) {
                    digest.update(buffer, 0, read);
                }
            }
        } finally {
            input.close();
        }

        StringBuilder hex = new StringBuilder(64);
        for (byte value : digest.digest()) {
            hex.append(String.format("%02x", value & 0xff));
        }
        return hex.toString();
    }
}

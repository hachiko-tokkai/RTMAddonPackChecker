package jp.rtm.addonpackchecker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ManifestComparator {
    private ManifestComparator() {
    }

    public static List<String> compare(List<PackEntry> server, List<PackEntry> client, boolean compareLastModified) {
        Map<String, PackEntry> serverByPath = index(server);
        Map<String, PackEntry> clientByPath = index(client);
        List<String> differences = new ArrayList<String>();

        for (Map.Entry<String, PackEntry> entry : serverByPath.entrySet()) {
            PackEntry clientEntry = clientByPath.remove(entry.getKey());
            if (clientEntry == null) {
                differences.add("クライアントにありません: " + entry.getKey());
                continue;
            }

            PackEntry serverEntry = entry.getValue();
            boolean contentDiffers = serverEntry.getSize() != clientEntry.getSize()
                    || !serverEntry.getSha256().equals(clientEntry.getSha256());
            boolean timeDiffers = compareLastModified
                    && serverEntry.getLastModified() != clientEntry.getLastModified();
            if (contentDiffers || timeDiffers) {
                differences.add("内容が異なります: " + entry.getKey()
                        + " [server " + describe(serverEntry)
                        + " / client " + describe(clientEntry) + "]");
            }
        }

        for (String extraPath : clientByPath.keySet()) {
            differences.add("クライアントにだけあります: " + extraPath);
        }
        return differences;
    }

    private static Map<String, PackEntry> index(List<PackEntry> entries) {
        Map<String, PackEntry> result = new LinkedHashMap<String, PackEntry>();
        for (PackEntry entry : entries) {
            result.put(entry.getPath(), entry);
        }
        return result;
    }

    private static String describe(PackEntry entry) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "更新=" + format.format(new Date(entry.getLastModified()))
                + ", size=" + entry.getSize()
                + ", SHA-256=" + entry.shortHash();
    }
}

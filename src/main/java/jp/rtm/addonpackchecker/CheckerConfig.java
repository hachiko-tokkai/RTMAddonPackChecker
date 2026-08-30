package jp.rtm.addonpackchecker;

import net.minecraftforge.common.config.Configuration;

public final class CheckerConfig {
    private final boolean compareLastModified;
    private final int maximumPacks;
    private final int maximumDifferencesInKickMessage;

    private CheckerConfig(boolean compareLastModified, int maximumPacks, int maximumDifferencesInKickMessage) {
        this.compareLastModified = compareLastModified;
        this.maximumPacks = maximumPacks;
        this.maximumDifferencesInKickMessage = maximumDifferencesInKickMessage;
    }

    public static CheckerConfig load(Configuration configuration) {
        configuration.load();
        boolean compareTime = configuration.getBoolean(
                "compareLastModified",
                Configuration.CATEGORY_GENERAL,
                false,
                "If true, equal content with different file timestamps is also rejected. Usually leave false."
        );
        int maximumPacks = configuration.getInt(
                "maximumPacks",
                Configuration.CATEGORY_GENERAL,
                2048,
                1,
                10000,
                "Safety limit for the number of detected RTM addon packs."
        );
        int maximumDifferences = configuration.getInt(
                "maximumDifferencesInKickMessage",
                Configuration.CATEGORY_GENERAL,
                8,
                1,
                30,
                "Maximum number of differences shown on the disconnect screen. All differences are written to the server log."
        );
        if (configuration.hasChanged()) {
            configuration.save();
        }
        return new CheckerConfig(compareTime, maximumPacks, maximumDifferences);
    }

    public boolean isCompareLastModified() {
        return compareLastModified;
    }

    public int getMaximumPacks() {
        return maximumPacks;
    }

    public int getMaximumDifferencesInKickMessage() {
        return maximumDifferencesInKickMessage;
    }
}

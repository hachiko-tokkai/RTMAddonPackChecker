package jp.rtm.addonpackchecker.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import jp.rtm.addonpackchecker.PackEntry;
import jp.rtm.addonpackchecker.PackScanner;
import jp.rtm.addonpackchecker.RTMAddonPackChecker;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class ManifestRequestMessageHandler implements IMessageHandler<ManifestRequestMessage, IMessage> {
    @Override
    public IMessage onMessage(ManifestRequestMessage message, MessageContext context) {
        try {
            List<PackEntry> entries = PackScanner.scan(
                    RTMAddonPackChecker.getModsDirectory(),
                    RTMAddonPackChecker.getConfig().getMaximumPacks()
            );
            return new ManifestMessage(entries, null);
        } catch (IOException error) {
            return new ManifestMessage(Collections.<PackEntry>emptyList(), error.getMessage());
        }
    }
}

package jp.rtm.addonpackchecker.network;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import jp.rtm.addonpackchecker.ManifestComparator;
import jp.rtm.addonpackchecker.PackEntry;
import jp.rtm.addonpackchecker.PackScanner;
import jp.rtm.addonpackchecker.RTMAddonPackChecker;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.List;

public final class ManifestMessageHandler implements IMessageHandler<ManifestMessage, IMessage> {
    @Override
    public IMessage onMessage(ManifestMessage message, MessageContext context) {
        EntityPlayerMP player = context.getServerHandler().playerEntity;
        if (message.getScanError() != null) {
            reject(player, "クライアント側でRTM追加パックを検査できませんでした: " + message.getScanError());
            return null;
        }

        final List<PackEntry> serverEntries;
        try {
            serverEntries = PackScanner.scan(
                    RTMAddonPackChecker.getModsDirectory(),
                    RTMAddonPackChecker.getConfig().getMaximumPacks()
            );
        } catch (IOException error) {
            reject(player, "サーバー側でRTM追加パックを検査できませんでした: " + error.getMessage());
            return null;
        }

        List<String> differences = ManifestComparator.compare(
                serverEntries,
                message.getEntries(),
                RTMAddonPackChecker.getConfig().isCompareLastModified()
        );
        if (!differences.isEmpty()) {
            FMLLog.log(RTMAddonPackChecker.MOD_ID, Level.WARN,
                    "RTM addon pack mismatch for player %s:", player.getCommandSenderName());
            for (String difference : differences) {
                FMLLog.log(RTMAddonPackChecker.MOD_ID, Level.WARN, "- %s", difference);
            }

            int maximum = RTMAddonPackChecker.getConfig().getMaximumDifferencesInKickMessage();
            StringBuilder reason = new StringBuilder("RTM追加パックがサーバーと一致しません。\n");
            for (int i = 0; i < differences.size() && i < maximum; i++) {
                reason.append("- ").append(limitLength(differences.get(i), 512)).append('\n');
            }
            if (differences.size() > maximum) {
                reason.append("ほか ").append(differences.size() - maximum).append(" 件。詳細はサーバーログを確認してください。");
            }
            reject(player, reason.toString());
        }
        return null;
    }

    private static void reject(EntityPlayerMP player, String reason) {
        FMLLog.log(RTMAddonPackChecker.MOD_ID, Level.WARN,
                "Rejecting player %s: %s", player.getCommandSenderName(), reason.replace('\n', ' '));
        player.playerNetServerHandler.kickPlayerFromServer(reason);
    }

    private static String limitLength(String value, int maximumLength) {
        if (value.length() <= maximumLength) {
            return value;
        }
        return value.substring(0, maximumLength - 3) + "...";
    }
}

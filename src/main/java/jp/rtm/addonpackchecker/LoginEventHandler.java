package jp.rtm.addonpackchecker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import jp.rtm.addonpackchecker.network.ManifestRequestMessage;
import net.minecraft.entity.player.EntityPlayerMP;

public final class LoginEventHandler {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            RTMAddonPackChecker.NETWORK.sendTo(new ManifestRequestMessage(), (EntityPlayerMP) event.player);
        }
    }
}

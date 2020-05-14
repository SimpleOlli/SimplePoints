package de.simpleolli.simplepoints.listener;

import de.simpleolli.simplepoints.SimplePoints;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void handle(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        SimplePoints.getInstance().getPointManager().safePlayer(player);
    }

}

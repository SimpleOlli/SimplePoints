package de.simpleolli.simplepoints.listener;

import de.simpleolli.simplepoints.SimplePoints;
import de.simpleolli.simplepoints.database.object.Transaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        SimplePoints.getInstance().getPointManager().cachePlayer(player);
    }
}

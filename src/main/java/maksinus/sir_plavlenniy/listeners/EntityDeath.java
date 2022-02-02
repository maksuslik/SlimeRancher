package maksinus.sir_plavlenniy.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityDeath implements Listener {
    @EventHandler
    public void death(PlayerDeathEvent e) {
        Player p = e.getEntity();
        ArmorStand stand = p.getWorld().spawn(p.getLocation().add(0, +2, 0), ArmorStand.class);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setMarker(true);
        stand.addScoreboardTag("custom");
        p.setPassenger(stand);
    }
}

package maksinus.sir_plavlenniy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawn implements Listener {
    @EventHandler
    public void spawn(CreatureSpawnEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_CAT_EAT, 1, 0);
        }
    }
}

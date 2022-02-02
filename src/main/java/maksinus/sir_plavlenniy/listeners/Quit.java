package maksinus.sir_plavlenniy.listeners;

import maksinus.sir_plavlenniy.Sir_plavlenniy;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {
    @EventHandler
    public void quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(!p.getPassengers().isEmpty()) {
            if(p.getPassenger() instanceof ArmorStand);
            ArmorStand stand = (ArmorStand) p.getPassenger();
            stand.remove();
        }
    }
}

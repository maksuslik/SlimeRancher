package maksinus.sir_plavlenniy.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class Block implements Listener {
    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if (!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }
}

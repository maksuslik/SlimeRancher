package maksinus.sir_plavlenniy.listeners;

import maksinus.sir_plavlenniy.Sir_plavlenniy;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

public class Interact implements Listener {

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack brickk = new ItemStack(Material.BRICK);
        ItemMeta brmeta = brickk.getItemMeta();
        brmeta.setDisplayName(Sir_plavlenniy.Color("&6Телефон"));
        brmeta.setLore(Arrays.asList(Sir_plavlenniy.Color("&7Бебра.")));
        brickk.setItemMeta(brmeta);
        if (p.getInventory().getItemInMainHand().equals(new ItemStack(Material.BRICK)) && p.getInventory().getItemInMainHand().hasItemMeta()) InventoryClick.phone(p);
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL || e.getPlayer().getGameMode() == GameMode.ADVENTURE && !e.getPlayer().isOp()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                switch (Objects.requireNonNull(e.getClickedBlock()).getType()) {
                    case WHITE_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopdirt");
                        return;
                    case ORANGE_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopzagon");
                        return;
                    case MAGENTA_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopchiken");
                        return;
                    case BROWN_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopsilos");
                        return;
                    case LIGHT_BLUE_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopfire");
                        return;
                    case YELLOW_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopwater");
                        return;
                    case LIME_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopmenu");
                        return;
                    case PINK_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shoples");
                        return;
                    case GRAY_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopgrot");
                        return;
                    case LIGHT_GRAY_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shoplaba");
                        return;
                    case CYAN_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopdok");
                        return;
                    case PURPLE_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopferma");
                        return;
                    case BLUE_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("telephone");
                        return;
                    case GREEN_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopupgrade");
                        return;
                    case RED_GLAZED_TERRACOTTA:
                        e.getPlayer().addScoreboardTag("shopgames");
                }
            }
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK && !e.getPlayer().isOp() && (e.getClickedBlock() instanceof TrapDoor || e.getClickedBlock() instanceof Door || e.getClickedBlock().getType() == Material.FLOWER_POT || e.getClickedBlock().getType() == Material.LEVER || e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock() instanceof Gate || e.getClickedBlock().getType() == Material.ARMOR_STAND)) {
            e.setCancelled(true);
        }
    }
}

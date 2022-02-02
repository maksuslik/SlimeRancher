package maksinus.sir_plavlenniy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {
    public static void removeItemsExact(Player p, ItemStack itemToRemove) {
        for (ItemStack item : p.getInventory().getStorageContents()) {
            if (item != null) {
                int size = p.getInventory().getSize();
                for (int slot = 0; slot < size; slot++) {
                    ItemStack is = p.getInventory().getItem(slot);
                    if (is == null) continue;
                    if (itemToRemove.getType() == is.getType()) {
                        p.getInventory().clear(slot);
                    }
                }
            }
        }
    }

    public static void sale(Player p, ItemStack item) {
        int i1 = 0;
        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null) {
                if (is.getType() == item.getType()) {
                    i1 += is.getAmount();
                }
            }
        }
        removeItemsExact(p, item);
        int sum = item.getAmount();
        Sir_plavlenniy.economy.depositPlayer(p, sum);
        p.sendMessage(ChatColor.GREEN + "+" + sum);
    }
}

package maksinus.sir_plavlenniy.listeners;

import maksinus.sir_plavlenniy.IslandWarps;
import maksinus.sir_plavlenniy.Players;
import maksinus.sir_plavlenniy.Sir_plavlenniy;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Join implements Listener {

    static Sir_plavlenniy main;
    static Players players = Sir_plavlenniy.getPlayersData();
    static IslandWarps is = Sir_plavlenniy.getData();
    public static int id;

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        String lastline = players.getLastLine(players.pfile);
        p.sendMessage(lastline);
        if (lastline == null) p.sendMessage("lastline is null");
        id = Integer.parseInt(lastline.replaceAll("\\D+", ""));
        if (!players.getConfig().contains("players." + p.getName())) {
            players.getConfig().set("players." + p.getName() + ".status", "owner");
            players.getConfig().set("players." + p.getName() + ".id", id + 1);
            players.getConfig().set("players." + p.getName() + ".islandId", id + 1);
            players.save();
            int id1 = id+1;
            is.getConfig().set("islandsWarps." + id1 + ".owner", p.getName());
            List<String> list = new ArrayList<>();
            main.setInviteList(list);
            main.addInvite(p);
            is.save();
            id++;
            p.setPlayerListName("[" + id + "] " + p.getName());
            p.teleport(new Location(Bukkit.getWorld(p.getWorld().getName()), 0, 100, id*1000));
            Objects.requireNonNull(Bukkit.getWorld(p.getWorld().getName())).getBlockAt(0, 99, id*1000).setType(Material.RED_CONCRETE);
        } else {
            p.sendMessage("Вы уже есть в БД!");
            p.setPlayerListName("[" + id + "] " + p.getName());
            p.sendMessage(String.valueOf(id));
            p.setPlayerListName("[" + players.getConfig().getInt("players." + p.getName() + ".islandId") + "] " + p.getName());
        }
        main.getInviteList(p);


        ItemStack brick = new ItemStack(Material.BRICK);
        ItemMeta brmeta = brick.getItemMeta();
        brmeta.setDisplayName(Sir_plavlenniy.Color("&6Телефон"));
        brmeta.setLore(Arrays.asList(Sir_plavlenniy.Color("&7Бебра.")));
        brick.setItemMeta(brmeta);
        p.getInventory().setItem(2, brick);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Sir_plavlenniy.getInstance(), new Runnable() {
            @Override
            public void run() {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Sir_plavlenniy.Color("&l⓪ " + (int) Sir_plavlenniy.economy.getBalance(p))));
            }
        }, 0, 20);

        int i = 8;
        while (i < 35) {
            i += 1;
            e.getPlayer().getInventory().setItem(i, new ItemStack(Material.BARRIER));
        }
        e.getPlayer().getInventory().setItem(1, new ItemStack(Material.BARRIER));
        e.getPlayer().getInventory().setItem(7, new ItemStack(Material.BARRIER));
        e.getPlayer().getInventory().setItem(0, new ItemStack(Material.BARRIER));
        e.getPlayer().getInventory().setItem(8, new ItemStack(Material.BARRIER));

    }
}

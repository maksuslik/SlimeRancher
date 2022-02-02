package maksinus.sir_plavlenniy.commands;

import maksinus.sir_plavlenniy.IslandWarps;
import maksinus.sir_plavlenniy.Sir_plavlenniy;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class IslandCommand implements CommandExecutor {

    private IslandWarps is;
    private Sir_plavlenniy main;
    HashMap<Player, Player> invites = new HashMap<>();
    int time = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if (args[0].equalsIgnoreCase("addtolist")) {
            if ((Sir_plavlenniy.getData().getConfig().getString("islandsWarps.") + p.getName()).contains(p.getName()) && is.getConfig().getString("islandsWarps." + p.getName()) != null) {
                p.sendMessage(Color("&cВаше ранчо уже есть в списке!"));
                Sir_plavlenniy.getData().getConfig().set("islandsWarps." + p.getName(), null);
                Sir_plavlenniy.getData().save();
            } else {
                p.sendMessage(p.getPlayerListName());
                p.sendMessage("Вы добавили свой остров в список!");
                String uuid = UUID.randomUUID().toString();
                Sir_plavlenniy.getData().getConfig().set("islandsWarps." + "1" + ".owner", p.getName());
                Sir_plavlenniy.getData().save();
            }
            return true;
        } else if (args[0].equalsIgnoreCase("invite")) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (existPlayer(target.getName(), p.getName())) {
                p.sendMessage("Данный игрок уже числится на острове!");
                return true;
            }
            if (invites.containsKey(p) && invites.containsValue(target)) {
                p.sendMessage("Приглашение уже отправлено!");
                return true;
            }

            target.sendMessage(Color("&a" + p.getName() + " &fприглашает Вас на своё ранчо!"));
            TextComponent accept = new TextComponent(Color("&7[&aПринять&7]"));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/is accept " + p.getName()));
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Принять заявку").create()));
            target.spigot().sendMessage(accept);
            TextComponent decline = new TextComponent(Color("&7[&4Отклонить&7]"));
            decline.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/is decline " + p.getName()));
            decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Отклонить заявку").create()));
            target.spigot().sendMessage(decline);

            invites.put(p, target);
            Bukkit.getScheduler().runTaskTimer(Sir_plavlenniy.getInstance(), task -> {
                if (!invites.containsKey(p)) {
                    time = 0;
                    task.cancel();
                    return;
                }
                if (time > 10) {
                    p.sendMessage("Время истекло.");
                    invites.remove(p, target);
                    time = 0;
                    task.cancel();
                }
                if (invites.containsKey(p) && invites.containsValue(Bukkit.getPlayerExact("declined"))) {
                    p.sendMessage("Приглашение отклонено!");
                    task.cancel();
                    return;
                }
                time++;
            }, 0, 20);
        } else if (args[0].equalsIgnoreCase("accept")) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (!invites.containsKey(target) && !invites.containsValue(p)) {
                p.sendMessage(invites.toString());
                p.sendMessage("Нет активных приглашений!");
                return true;
            }
            target.sendMessage(Color("&a" + p.getName() + " &fпринял вашу заявку!"));
            p.sendMessage(Color("Теперь вы принадлежите &a" + target.getName()));
            Sir_plavlenniy.getPlayersData().getConfig().set("players." + p.getName() + ".islandId", Sir_plavlenniy.getPlayersData().getConfig().get("players." + target.getName() + ".id"));
            Sir_plavlenniy.getPlayersData().getConfig().set("players." + p.getName() + ".status", "default");
            Sir_plavlenniy.getPlayersData().save();
            invites.remove(p, target);
            List<String> list = Sir_plavlenniy.getData().getConfig().getStringList("islandsWarps." + Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + target.getName() + ".islandId") + ".players");
            list.add(p.getName());
            Sir_plavlenniy.getData().getConfig().set("islandsWarps." + Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + p.getName() + ".islandId") + ".players", list);
            Sir_plavlenniy.getData().save();
        } else if (args[0].equalsIgnoreCase("decline")) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (!invites.containsKey(p) && !invites.containsValue(target)) {
                p.sendMessage("Нет активных приглашений!");
                return true;
            }
            p.sendMessage(Color("&a" + target.getName() + " &отклонил вашу заявку."));
            target.sendMessage(Color("&cЗаявка отклонена."));
            invites.remove(p, target);
            invites.put(p, Bukkit.getPlayerExact("declined"));
        } else if (args[0].equalsIgnoreCase("info")) {
            Inventory infoInv = Bukkit.createInventory(null, 27, "Ранчо");
            for (int slot = 0; slot < 9; slot++) {
                infoInv.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
            int islandId = Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + p.getName() + ".islandId");
            OfflinePlayer owner = Bukkit.getOfflinePlayer(Objects.requireNonNull(Sir_plavlenniy.getData().getConfig().getString("islandsWarps." + islandId + ".owner")));
            ItemStack info = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta imeta = info.getItemMeta();
            imeta.setDisplayName(Color("&eРанчо #" + islandId));
            imeta.setLore(Arrays.asList(Color("&fВладелец: &a" + owner.getName())));
            info.setItemMeta(imeta);
            infoInv.setItem(4, info);

            ItemStack leave = new ItemStack(Material.ARROW);
            ItemMeta lmeta = leave.getItemMeta();
            lmeta.setDisplayName(Color("&cПокинуть остров"));
            leave.setItemMeta(lmeta);
            infoInv.setItem(8, leave);

            List<String> playersOnIsland = Sir_plavlenniy.getData().getConfig().getStringList("islandsWarps." + Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + p.getName() + ".islandId") + ".players");
            for (int i = 0; i < playersOnIsland.size(); i++) {
                OfflinePlayer player = Bukkit.getOfflinePlayer((String) playersOnIsland.toArray()[i]);
                String status = Sir_plavlenniy.getPlayersData().getConfig().getString("players." + player.getName() + ".status");
                SkullMeta skull = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                skull.setOwningPlayer(player);
                ArrayList<String> lore = new ArrayList<String>();
                if (player.isOnline()) {
                    lore.add(ChatColor.GREEN + "Онлайн");
                } else lore.add(ChatColor.RED + "Оффлайн");
                lore.add("");
                if (status.equalsIgnoreCase("owner")) lore.add(Color("&cВладелец"));
                else if (status.equalsIgnoreCase("coowner")) lore.add(Color("&eСовладелец"));
                else lore.add(Color("&bУчастник"));
                lore.add("");
                lore.add(ChatColor.GRAY + "ЛКМ » Повысить в должности");
                lore.add(ChatColor.GRAY + "ПКМ » Кикнуть");
                skull.setLore(lore);
                ItemStack listedPlayers = new ItemStack(Material.PLAYER_HEAD, 1);
                skull.setDisplayName(player.getName());
                listedPlayers.setItemMeta(skull);
                infoInv.setItem(i + 9, listedPlayers);
            }
            p.openInventory(infoInv);
            p.sendMessage(playersOnIsland.toString());
            return true;

        } else if (args[0].equalsIgnoreCase("kick")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            Sir_plavlenniy.getPlayersData().getConfig().set("players." + target.getName() + ".islandId", Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + target.getName() + ".id"));
            Sir_plavlenniy.getPlayersData().save();
            List<String> playersOnIsland = Sir_plavlenniy.getData().getConfig().getStringList("islandsWarps." + Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + p.getName() + ".islandId") + ".players");
            if (playersOnIsland.contains(target.getName())) {
                playersOnIsland.remove(target.getName());
                Sir_plavlenniy.getData().getConfig().set("islandsWarps." + Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + p.getName() + ".islandId") + ".players", playersOnIsland);
                Sir_plavlenniy.getData().save();
                Sir_plavlenniy.getPlayersData().getConfig().set("players." + target.getName() + ".status", "owner");
                Sir_plavlenniy.getPlayersData().getConfig().set("players." + target.getName() + ".islandId", Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + target.getName() + ".id"));
                Sir_plavlenniy.getPlayersData().save();
            } else {
                p.sendMessage(target.getName());
                p.sendMessage("Игрока нет на острове!");
                p.sendMessage(playersOnIsland.toString());
            }
        }
        return true;
    }

    private boolean existPlayer(String player, String target) {
        for (String s : Sir_plavlenniy.getData().getConfig().getStringList("islandsWarps." + Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + player + ".islandId") + ".players")) {
            if (target.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private static String Color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public void leaveIsland(OfflinePlayer player) {
        Sir_plavlenniy.getPlayersData().getConfig().set("players." + player.getName() + ".islandId", Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + player.getName() + ".id"));
        Sir_plavlenniy.getPlayersData().save();
        List<String> playersOnIsland = Sir_plavlenniy.getData().getConfig().getStringList("islandsWarps." + Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + player.getName() + ".islandId") + ".players");
        if (playersOnIsland.contains(player.getName())) {
            playersOnIsland.remove(player.getName());
            Sir_plavlenniy.getData().getConfig().set("islandsWarps." + Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + player.getName() + ".islandId") + ".players", playersOnIsland);
            Sir_plavlenniy.getData().save();
            Sir_plavlenniy.getPlayersData().getConfig().set("players." + player.getName() + ".status", "owner");
            Sir_plavlenniy.getPlayersData().getConfig().set("players." + player.getName() + ".islandId", Sir_plavlenniy.getPlayersData().getConfig().getInt("players." + player.getName() + ".id"));
            Sir_plavlenniy.getPlayersData().save();
        }
    }
}

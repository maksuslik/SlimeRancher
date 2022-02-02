package maksinus.sir_plavlenniy.listeners;

import maksinus.sir_plavlenniy.IslandWarps;
import maksinus.sir_plavlenniy.Players;
import maksinus.sir_plavlenniy.Sir_plavlenniy;
import maksinus.sir_plavlenniy.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryClick implements Listener {

    static Players players = Sir_plavlenniy.getPlayersData();
    static IslandWarps is = Sir_plavlenniy.getData();
    private Inventory whitelist = Bukkit.createInventory(null, 54, "Игроки");

    @EventHandler
    public void inv(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("Телефон")) {
            e.setCancelled(true);
            switch (Objects.requireNonNull(e.getCurrentItem()).getType()) {
                case QUARTZ:
                    Inventory islandsmenu = Bukkit.createInventory(null, 54, "Список ранчо");
                    try {
                        int slot = 0;
                        for (String plnames : Objects.requireNonNull(is.getConfig().getConfigurationSection("islandsWarps")).getKeys(false)) {
                            Player all = Bukkit.getPlayerExact(plnames);
                            ItemStack sign = new ItemStack(Material.OAK_SIGN);
                            ItemMeta meta = sign.getItemMeta();
                            ArrayList<String> lore = new ArrayList<String>();
                            lore.add(ChatColor.WHITE + "Владелец » " + ChatColor.AQUA + is.getConfig().getConfigurationSection("islandsWarps").getKeys(false).toArray()[slot]);
                            assert all != null;
                            if (all.isOnline()) {
                                lore.add(ChatColor.GREEN + "Онлайн");
                            } else lore.add(ChatColor.RED + "Оффлайн");
                            meta.setLore(lore);
                            meta.setDisplayName(ChatColor.YELLOW + "Ранчо #" + is.getConfig().getInt("islandsWarps." + p.getName() + ".id"));
                            sign.setItemMeta(meta);
                            islandsmenu.setItem(slot, sign);
                            slot += 1;
                        }
                    } catch (NullPointerException err) {
                        p.openInventory(islandsmenu);
                    }
                    p.openInventory(islandsmenu);
                    return;
                case OAK_SIGN:
                    whitelist = Bukkit.createInventory(null, 54, "Игроки");
                    int slot = 0;
                    for (OfflinePlayer all : Bukkit.getWhitelistedPlayers()) {
                        SkullMeta skull = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                        skull.setOwningPlayer(all);
                        ArrayList<String> lore = new ArrayList<String>();
                        if (all.isOnline()) {
                            lore.add(ChatColor.GREEN + "Онлайн");
                        } else lore.add(ChatColor.RED + "Оффлайн");
                        lore.add(ChatColor.DARK_GRAY + "ЛКМ » Пригласить на остров");
                        lore.add(ChatColor.DARK_GRAY + "ПКМ » Дополнительная информация");
                        skull.setLore(lore);
                        ItemStack listedPlayers = new ItemStack(Material.PLAYER_HEAD, 1);
                        skull.setDisplayName(all.getName());
                        listedPlayers.setItemMeta(skull);
                        whitelist.setItem(slot, listedPlayers);
                        slot += 1;

                    }

                    p.openInventory(whitelist);
            }
        }
        if (e.getView().getTitle().equalsIgnoreCase("Список ранчо")) {
            e.setCancelled(true);
            Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
            Matcher matcher = pat.matcher(Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName());
            Location ranchoLoc = null;
            while (matcher.find()) {
                ranchoLoc = new Location(Bukkit.getWorld("world"), 0, 100, Integer.parseInt(matcher.group()) * 1000);
            }
            assert ranchoLoc != null;
            p.teleport(ranchoLoc);
        }

        if (e.getView().getTitle().equalsIgnoreCase("Игроки")) {
            e.setCancelled(true);
            OfflinePlayer playername = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName());
            Inventory playerInv = Bukkit.createInventory(null, 9, "Информация об игроке");
            // голова
            SkullMeta skull = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
            skull.setOwningPlayer(playername);
            ArrayList<String> lore = new ArrayList<String>();
            if (playername.isOnline()) {
                lore.add(ChatColor.GREEN + "Онлайн");
            } else lore.add(ChatColor.RED + "Оффлайн");
            lore.add("");
            lore.add(ChatColor.GOLD + "1 lvl");
            skull.setLore(lore);
            ItemStack listedPlayers = new ItemStack(Material.PLAYER_HEAD, 1);
            skull.setDisplayName(ChatColor.WHITE + playername.getName());
            listedPlayers.setItemMeta(skull);
            playerInv.setItem(4, listedPlayers);
            // назад
            ItemStack back = new ItemStack(Material.ARROW);
            ItemMeta bmeta = back.getItemMeta();
            bmeta.setDisplayName(Sir_plavlenniy.Color("&cНазад"));
            back.setItemMeta(bmeta);
            playerInv.setItem(0, back);
            // добавить на остров
            ItemStack isadd = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta ismeta = isadd.getItemMeta();
            ismeta.setDisplayName(Sir_plavlenniy.Color("&aДобавить на остров"));
            isadd.setItemMeta(ismeta);
            playerInv.setItem(3, isadd);
            // удалить с острова
            ItemStack isremove = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta rmeta = isremove.getItemMeta();
            rmeta.setDisplayName(Sir_plavlenniy.Color("&cУдалить с острова"));
            isremove.setItemMeta(rmeta);
            playerInv.setItem(5, isremove);
            if(p.isOp()) {
                // кикнуть
                ItemStack kick = new ItemStack(Material.PINK_TERRACOTTA);
                ItemMeta kmeta = kick.getItemMeta();
                kmeta.setDisplayName(Sir_plavlenniy.Color("&6Кикнуть"));
                kick.setItemMeta(kmeta);
                playerInv.setItem(7, kick);
                // забанить
                ItemStack ban = new ItemStack(Material.RED_TERRACOTTA);
                ItemMeta banmeta = ban.getItemMeta();
                banmeta.setDisplayName(Sir_plavlenniy.Color("&4Забанить"));
                ban.setItemMeta(banmeta);
                playerInv.setItem(8, ban);
            }
            p.openInventory(playerInv);
        }
        if(e.getView().getTitle().equalsIgnoreCase("Информация об игроке")) {
            e.setCancelled(true);
            String target_name = Objects.requireNonNull(Objects.requireNonNull(e.getInventory().getItem(4)).getItemMeta()).getDisplayName().trim().substring(2);
            OfflinePlayer target = Bukkit.getOfflinePlayer(target_name);
            switch (e.getCurrentItem().getType()) {
                case ARROW:
                    p.openInventory(whitelist);
                    return;
                case LIME_STAINED_GLASS_PANE:
                    if(target == null) p.sendMessage("Игрок оффлайн!");
                    p.chat("/is invite " + target.getName());
                    return;
                case RED_STAINED_GLASS_PANE:
                    p.sendMessage("В разработке");
                    return;
                case PINK_TERRACOTTA:
                    if(target == null) p.sendMessage("Игрок оффлайн!");
                    else p.chat("/kick " + Bukkit.getOfflinePlayer(target_name).getName()); p.sendMessage(String.valueOf(Bukkit.getOfflinePlayer(target_name).getName()));
                    return;
                case RED_TERRACOTTA:
                    if(target == null) p.sendMessage("Игрок оффлайн!");
                    p.chat("/ban " + target.getName());
                    return;
            }
        }
        if(e.getView().getTitle().equalsIgnoreCase("Ранчо")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName());
                String status = Sir_plavlenniy.getPlayersData().getConfig().getString("players." + player.getName() + ".status");
                if(!(player == p)) {
                    if (status.equalsIgnoreCase("owner")) {
                        Sir_plavlenniy.getPlayersData().getConfig().set("players." + player.getName() + ".status", "default");
                        Sir_plavlenniy.getPlayersData().save();
                        p.updateInventory();
                    }
                    else if (status.equalsIgnoreCase("coowner")) {
                        Sir_plavlenniy.getPlayersData().getConfig().set("players." + player.getName() + ".status", "owner");
                        Sir_plavlenniy.getPlayersData().save();
                        p.updateInventory();
                    } else if (status.equalsIgnoreCase("default")) {
                        Sir_plavlenniy.getPlayersData().getConfig().set("players." + player.getName() + ".status", "coowner");
                        Sir_plavlenniy.getPlayersData().save();
                        p.updateInventory();
                    }
                }
            } else if(e.getCurrentItem().getType() == Material.ARROW) {
                p.chat("/is leave");
            }
        }
        if (e.getView().getTitle().equalsIgnoreCase("Продажа")) {
            e.setCancelled(true);
            // с instanceof не работает, не бей((
            if (e.getInventory().contains(Material.CHICKEN_SPAWN_EGG) || e.getInventory().contains(Material.COD_SPAWN_EGG) || e.getInventory().contains(Material.COW_SPAWN_EGG) || e.getInventory().contains(Material.CREEPER_SPAWN_EGG) || e.getInventory().contains(Material.DOLPHIN_SPAWN_EGG) || e.getInventory().contains(Material.DONKEY_SPAWN_EGG) || e.getInventory().contains(Material.DROWNED_SPAWN_EGG) || e.getInventory().contains(Material.ELDER_GUARDIAN_SPAWN_EGG) || e.getInventory().contains(Material.ENDERMAN_SPAWN_EGG) || e.getInventory().contains(Material.ENDERMITE_SPAWN_EGG) || e.getInventory().contains(Material.EVOKER_SPAWN_EGG) || e.getInventory().contains(Material.FOX_SPAWN_EGG) || e.getInventory().contains(Material.GHAST_SPAWN_EGG) || e.getInventory().contains(Material.GLOW_SQUID_SPAWN_EGG) || e.getInventory().contains(Material.GOAT_SPAWN_EGG) || e.getInventory().contains(Material.GUARDIAN_SPAWN_EGG) || e.getInventory().contains(Material.HOGLIN_SPAWN_EGG) || e.getInventory().contains(Material.HORSE_SPAWN_EGG)) {
                switch (Objects.requireNonNull(e.getCurrentItem()).getAmount()) {
                    case 1:
                        Utils.sale(p, new ItemStack(Material.CHICKEN_SPAWN_EGG));
                        return;
                    case 2:
                        Utils.sale(p, new ItemStack(Material.COD_SPAWN_EGG));
                        return;
                    case 3:
                        Utils.sale(p, new ItemStack(Material.COW_SPAWN_EGG));
                        return;
                    case 4:
                        Utils.sale(p, new ItemStack(Material.CREEPER_SPAWN_EGG));
                        return;
                    case 5:
                        Utils.sale(p, new ItemStack(Material.DOLPHIN_SPAWN_EGG));
                        return;
                    case 6:
                        Utils.sale(p, new ItemStack(Material.DONKEY_SPAWN_EGG));
                        return;
                    case 7:
                        Utils.sale(p, new ItemStack(Material.DROWNED_SPAWN_EGG));
                        return;
                    case 8:
                        Utils.sale(p, new ItemStack(Material.ELDER_GUARDIAN_SPAWN_EGG));
                        return;
                    case 9:
                        Utils.sale(p, new ItemStack(Material.ENDERMAN_SPAWN_EGG));
                        return;
                    case 10:
                        Utils.sale(p, new ItemStack(Material.ENDERMITE_SPAWN_EGG));
                        return;
                    case 11:
                        Utils.sale(p, new ItemStack(Material.EVOKER_SPAWN_EGG));
                        return;
                    case 12:
                        Utils.sale(p, new ItemStack(Material.FOX_SPAWN_EGG));
                        return;
                    case 13:
                        Utils.sale(p, new ItemStack(Material.GHAST_SPAWN_EGG));
                        return;
                    case 14:
                        Utils.sale(p, new ItemStack(Material.GLOW_SQUID_SPAWN_EGG));
                        return;
                    case 15:
                        Utils.sale(p, new ItemStack(Material.GOAT_SPAWN_EGG));
                        return;
                    case 16:
                        Utils.sale(p, new ItemStack(Material.GUARDIAN_SPAWN_EGG));
                        return;
                    case 17:
                        Utils.sale(p, new ItemStack(Material.HOGLIN_SPAWN_EGG));
                        return;
                    case 18:
                        Utils.sale(p, new ItemStack(Material.HORSE_SPAWN_EGG));
                }
            }
        }

    }

    @EventHandler
    public void invdrag(InventoryDragEvent e) {
        if (!e.getWhoClicked().isOp()) e.setCancelled(true);
    }

    public static void phone(Player p) {
        Inventory phone = Bukkit.createInventory(null, 27, "Телефон");
        ItemStack farms = new ItemStack(Material.QUARTZ);
        ItemMeta fmeta = farms.getItemMeta();
        fmeta.setDisplayName(Sir_plavlenniy.Color("&eФермы"));
        fmeta.setLore(Arrays.asList(Sir_plavlenniy.Color("&7Информация о фермах других игроков")));
        farms.setItemMeta(fmeta);
        phone.setItem(11, farms);

        ItemStack friends = new ItemStack(Material.OAK_SIGN);
        ItemMeta frmeta = friends.getItemMeta();
        frmeta.setDisplayName(Sir_plavlenniy.Color("&3Пригласить друга"));
        frmeta.setLore(Arrays.asList(Sir_plavlenniy.Color("&7Пригласите друга на своё ранчо, хуле!")));
        friends.setItemMeta(frmeta);
        phone.setItem(13, friends);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta bmeta = barrier.getItemMeta();
        bmeta.setDisplayName(Sir_plavlenniy.Color("&cВ разработке"));
        bmeta.setLore(Arrays.asList(Sir_plavlenniy.Color("&7Тут шота буит")));
        barrier.setItemMeta(bmeta);
        phone.setItem(15, barrier);

        p.openInventory(phone);
    }

}

package maksinus.sir_plavlenniy;

import maksinus.sir_plavlenniy.commands.EconomyCommand;
import maksinus.sir_plavlenniy.commands.IslandCommand;
import maksinus.sir_plavlenniy.listeners.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Sir_plavlenniy extends JavaPlugin implements Listener, CommandExecutor {

    public static Economy economy;
    private IslandWarps data;
    private Players playersData;
    public static Sir_plavlenniy instance;

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (!setupEconomy()) {
            Bukkit.shutdown();
        }
        Bukkit.getPluginManager().registerEvents(new Block(), this);
        Bukkit.getPluginManager().registerEvents(new EntitySpawn(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeath(), this);
        Bukkit.getPluginManager().registerEvents(new Interact(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new Join(), this);
        Bukkit.getPluginManager().registerEvents(new Quit(), this);

        getCommand("is").setExecutor(new IslandCommand());
        getCommand("ecc").setExecutor(new EconomyCommand());

        instance = this;

        data = new IslandWarps("islandsWarps.yml");
        playersData = new Players("players.yml");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled!");
    }

    public static String Color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static IslandWarps getData() {
        return instance.data;
    }

    public static Players getPlayersData() {
        return instance.playersData;
    }

    public static Sir_plavlenniy getInstance() {
        return instance;
    }

    public void setInviteList(List<String> inviteList) {
        getData().getConfig().set("islandsWarps." + (Join.id+1) + ".players", inviteList);
        getData().save();
    }

    public List<String> getInviteList(Player p) {
        return getData().getConfig().getStringList("islandsWarps." + getPlayersData().getConfig().getInt("players." + p.getName() + ".islandId") + ".players");
    }

    public void addInvite(Player p) {
        List<String> list = getInviteList(p);
        list.add(p.getName());

        setInviteList(list);

    }

    public void removeInvite(Player p) {
        List<String> list = getInviteList(p);
        list.remove(p.getName());

        setInviteList(list);
    }
}




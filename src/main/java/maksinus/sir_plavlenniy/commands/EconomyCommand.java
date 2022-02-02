package maksinus.sir_plavlenniy.commands;

import maksinus.sir_plavlenniy.Sir_plavlenniy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class EconomyCommand implements CommandExecutor {
    private Economy economy = Sir_plavlenniy.economy;
    Sir_plavlenniy main;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(command.getName().equalsIgnoreCase("ecc")) {
                if(args[0].equalsIgnoreCase("award")) {
                    if(args.length == 3) {
                        try {
                            Player target = Bukkit.getPlayer(args[1]);
                            int depositAmount = Integer.parseInt(args[2]);

                            if (economy.hasAccount(target)) {
                                Sir_plavlenniy.economy.depositPlayer(target, depositAmount);
                                p.sendMessage(Color("&7Вы подарили &a " + target.getName() + " " + depositAmount));
                                target.sendMessage(Color("&a" + p.getName() + " &7подарил вам &a " + depositAmount));
                                updateScoreboard(target);
                            } else {
                                p.sendMessage(Color("&cУ игрока нет счета!"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("balance")) {
                    if(args.length < 1) {
                        int balance = (int) economy.getBalance(p);
                        p.sendMessage(Color("&7Ваш баланс: &a " + balance));
                    if(args.length >= 1) {
                        try {
                            Player target = Bukkit.getPlayer(args[1]);
                            p.sendMessage("&7Баланс &a" + target.getName() + "&7: &a " + balance);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        }
                    }
                }
            }
        }
        return true;
    }

    private String Color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private void updateScoreboard(Player p) {
        Scoreboard s = p.getScoreboard();
        Objective o = s.getObjective("slime");
        assert o != null;
        for(String e : s.getEntries()) {
            s.resetScores(e);
        }
        o.getScore(Color("&a")).setScore(3);
        o.getScore(Color("Монет 💸: &e" + (int) economy.getBalance(p))).setScore(2);
        o.getScore("").setScore(1);
        o.getScore(Color("&b100 ⚡      ")).setScore(0);
    }
}

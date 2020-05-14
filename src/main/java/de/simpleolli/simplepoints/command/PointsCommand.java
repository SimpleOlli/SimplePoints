package de.simpleolli.simplepoints.command;

import de.simpleolli.simplepoints.SimplePoints;
import de.simpleolli.simplepoints.database.object.PointPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            if(player.hasPermission("System.usePoints")) {
                PointPlayer pointPlayer = SimplePoints.getInstance().getPointManager().getPlayerCache().get(player);
                if(strings.length == 0) {
                    player.sendMessage(SimplePoints.getInstance().getPrefix() + "Du hast momentan §6" + pointPlayer.getPoints() + " §7Punkte");
                }else if(strings.length == 1) {
                    if(strings[0].equalsIgnoreCase("transactions") || strings[0].equalsIgnoreCase("überweisungen")) {
                        player.sendMessage(SimplePoints.getInstance().getPrefix() + "Du hast insgesamt §6" + pointPlayer.getTransactions().size() + " §7Transaktion(en) getätigt");
                    }else if(Bukkit.getPlayer(strings[0]) != null) {
                        player.sendMessage(SimplePoints.getInstance().getPrefix() + "Der Spieler §6" + strings[0] + " §7hat §6" + SimplePoints.getInstance().getPointManager().getPoints(Bukkit.getPlayer(strings[0])) + " §7Punkte");
                    }else {
                        player.sendMessage(SimplePoints.getInstance().getPrefix() + "Verwende: §b/points <Spieler | Transactions>");
                    }
                }else if(strings.length == 3) {
                    if(strings[0].equalsIgnoreCase("send")) {
                        Player target = Bukkit.getPlayer(strings[1]);
                        if(target != null) {
                            if(target != player) {
                                try {
                                    Integer points = Integer.valueOf(strings[2]);
                                    if(SimplePoints.getInstance().getPointManager().hasEnough(player,points)) {
                                        player.sendMessage(SimplePoints.getInstance().getPrefix() + "§6" + target.getName() + " §7wurden §6" + strings[2] + " §7Punkte überwiesen");
                                        target.sendMessage(SimplePoints.getInstance().getPrefix() + "§6" + player.getName() + " §7hat dir §6" + strings[2] + " §7Punkte überwiesen");
                                        SimplePoints.getInstance().getPointManager().sendPoints(player,target,points);
                                    }else {
                                        player.sendMessage(SimplePoints.getInstance().getPrefix() + "Du hast nicht genügend Punkte!");
                                    }
                                } catch (NumberFormatException exception) {
                                    player.sendMessage(SimplePoints.getInstance().getPrefix() + "§6" + strings[2] + " §7ist keine Zahl!");
                                }
                            }else {
                                player.sendMessage(SimplePoints.getInstance().getPrefix() + "Du kannst dir keine Coins überweisen!");
                            }
                        }else {
                            player.sendMessage(SimplePoints.getInstance().getPrefix() + "Der Spieler ist aktuell nicht online");
                        }
                    }else {
                        player.sendMessage(SimplePoints.getInstance().getPrefix() + "Verwende: §b/points Send <Spieler> <Anzahl>");
                    }
                }else {
                    player.sendMessage(SimplePoints.getInstance().getPrefix() + "Verwende: §b/points <Send | Transactions> <Spieler> <Anzahl>");
                }
            }else {
                player.sendMessage(SimplePoints.getInstance().getPrefix() + "§cDu wurdest aus dem PunkteSystem ausgeschlossen!");
            }
        }
        return false;
    }
}

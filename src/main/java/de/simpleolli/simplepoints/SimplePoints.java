package de.simpleolli.simplepoints;

import de.simpleolli.simplepoints.command.PointsCommand;
import de.simpleolli.simplepoints.database.MySQLManager;
import de.simpleolli.simplepoints.listener.PlayerJoinListener;
import de.simpleolli.simplepoints.listener.PlayerQuitListener;
import de.simpleolli.simplepoints.manager.PointManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimplePoints extends JavaPlugin {

    private static SimplePoints instance;
    private String prefix = "§8[§6Points§8] §7";

    private PointManager pointManager;
    private MySQLManager mySQLManager;

    @Override
    public void onEnable() {
        pointManager = new PointManager();
        mySQLManager = new MySQLManager("localhost","test","SimpleOlli","12345");
        mySQLManager.connect();
        registerListener();
        registerCommands();
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onDisable() {
        mySQLManager.disconnect();
    }

    private void registerCommands() {
        getCommand("points").setExecutor(new PointsCommand());
    }

    private void registerListener() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(),this);
        pluginManager.registerEvents(new PlayerQuitListener(),this);
    }

    public PointManager getPointManager() {
        return pointManager;
    }

    public MySQLManager getMySQLManager() {
        return mySQLManager;
    }

    public String getPrefix() {
        return prefix;
    }

    public static SimplePoints getInstance() {
        return instance;
    }
}

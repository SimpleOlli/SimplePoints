package de.simpleolli.simplepoints.database;

import de.simpleolli.simplepoints.SimplePoints;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLManager {

    private String host;
    private String database;
    private String userName;
    private String password;
    private Connection connection;


    public MySQLManager(String host, String database, String userName, String password) {
        this.host = host;
        this.database = database;
        this.userName = userName;
        this.password = password;
    }

    public void connect() {
        Bukkit.getConsoleSender().sendMessage(SimplePoints.getInstance().getPrefix() + "Versuche mit Datenbank zu verbinden....");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database,userName,password);
            createDefaultTable("CREATE TABLE IF NOT EXISTS pointPlayer (UUID VARCHAR(100),Points INT(10),transactionCount INT(5))");
            createDefaultTable("CREATE TABLE IF NOT EXISTS pointTransaction (ID VARCHAR(16),senderName VARCHAR(16),receiverName VARCHAR(16),pointCount INT(10),transactionDate VARCHAR(16))");
            Bukkit.getConsoleSender().sendMessage(SimplePoints.getInstance().getPrefix() + "§aErfolgreich mit Datenbank verbunden!");
        } catch (SQLException throwables) {
            Bukkit.getConsoleSender().sendMessage(SimplePoints.getInstance().getPrefix() + "§cEs konnte keine Verbindung zur Datenbank hergestellt werden!");
        }
    }

    private void createDefaultTable(final String tableQuery) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(tableQuery);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
            Bukkit.getConsoleSender().sendMessage(SimplePoints.getInstance().getPrefix() + "§cDie Verbindung zur Datenbank wurde geschlossen!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }
}

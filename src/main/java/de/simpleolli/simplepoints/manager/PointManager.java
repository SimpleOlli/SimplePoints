package de.simpleolli.simplepoints.manager;

import de.simpleolli.simplepoints.SimplePoints;
import de.simpleolli.simplepoints.database.object.PointPlayer;
import de.simpleolli.simplepoints.database.object.Transaction;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PointManager {

    private HashMap<Player, PointPlayer> playerCache;

    public PointManager() {
        this.playerCache = new HashMap<>();
    }

    public void cachePlayer(final Player player) {
        try {
            PreparedStatement preparedStatement = SimplePoints.getInstance().getMySQLManager().getConnection().prepareStatement("SELECT * FROM pointPlayer WHERE UUID=?");
            preparedStatement.setString(1,player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                PointPlayer pointPlayer = new PointPlayer(player.getUniqueId());
                pointPlayer.setTransactionCount(resultSet.getInt("transactionCount"));
                pointPlayer.setPoints(resultSet.getInt("points"));
                this.playerCache.put(player,pointPlayer);
            }else {
                this.playerCache.put(player,createDefaultPlayer(player.getUniqueId()));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void safePlayer(final Player player) {
        PointPlayer pointPlayer = this.playerCache.get(player);
        try {
            PreparedStatement preparedStatement = SimplePoints.getInstance().getMySQLManager().getConnection().prepareStatement("UPDATE pointPlayer SET points=?,transactionCount=? WHERE UUID=?");
            preparedStatement.setInt(1,pointPlayer.getPoints());
            preparedStatement.setInt(2,pointPlayer.getTransactionCount());
            preparedStatement.setString(3,player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        safeTransactions(pointPlayer);
        this.playerCache.remove(player);
    }

    private void safeTransactions(final PointPlayer pointPlayer) {
        if(!pointPlayer.getTransactions().isEmpty()) {
            try {
                PreparedStatement preparedStatement = SimplePoints.getInstance().getMySQLManager().getConnection().prepareStatement("INSERT INTO pointTransaction (ID,senderName,receiverName,pointCount,transactionDate) VALUES (?,?,?,?,?)");
                for(Transaction transaction : pointPlayer.getTransactions()) {
                    preparedStatement.setString(1,transaction.getTransactionID());
                    preparedStatement.setString(2,transaction.getSenderName());
                    preparedStatement.setString(3,transaction.getReceiverName());
                    preparedStatement.setInt(4,transaction.getPointCount());
                    preparedStatement.setString(5,transaction.getDateString());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }

    private PointPlayer createDefaultPlayer(final UUID playerUUID) {
        PointPlayer pointPlayer = new PointPlayer(playerUUID);
        pointPlayer.setPoints(100);
        pointPlayer.setTransactionCount(0);

        try {
            PreparedStatement preparedStatement = SimplePoints.getInstance().getMySQLManager().getConnection().prepareStatement("INSERT INTO pointPlayer (UUID,points,transactionCount) VALUES (?, ?, ?)");
            preparedStatement.setString(1,playerUUID.toString());
            preparedStatement.setInt(2,pointPlayer.getPoints());
            preparedStatement.setInt(3,pointPlayer.getTransactionCount());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return pointPlayer;
    }

    public void sendPoints(final Player player, final Player target, final Integer amount) {
        final PointPlayer playerPoint = playerCache.get(player);
        final PointPlayer targetPoint = playerCache.get(target);

        addPoints(target,amount);
        removeCoins(player,amount);

        playerPoint.setTransactionCount(playerPoint.getTransactionCount()+1);
        targetPoint.setTransactionCount(targetPoint.getTransactionCount()+1);
        targetPoint.getTransactions().add(new Transaction(player.getName(),target.getName(),amount));
        playerPoint.getTransactions().add(new Transaction(target.getName(),player.getName(),amount));
    }

    public boolean hasEnough(final Player player, final Integer points) {
        return playerCache.get(player).getPoints() >= points;
    }

    public Integer getPoints(final Player player) {
        return playerCache.get(player).getPoints();
    }

    public void addPoints(final Player player, final Integer points) {
        final PointPlayer cachePlayer = playerCache.get(player);
        cachePlayer.setPoints(cachePlayer.getPoints()+points);
    }

    public void removeCoins(final Player player, final Integer points) {
        final PointPlayer cachePlayer = playerCache.get(player);
        cachePlayer.setPoints(cachePlayer.getPoints()-points);
    }

    public HashMap<Player, PointPlayer> getPlayerCache() {
        return playerCache;
    }
}

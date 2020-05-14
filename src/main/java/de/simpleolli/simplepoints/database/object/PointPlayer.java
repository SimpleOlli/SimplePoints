package de.simpleolli.simplepoints.database.object;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PointPlayer {

    private UUID uuid;
    private Integer points;
    private Integer transactionCount;
    private ArrayList<Transaction> transactions;

    public PointPlayer(final UUID uuid) {
        this.uuid = uuid;
        this.transactions = new ArrayList<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

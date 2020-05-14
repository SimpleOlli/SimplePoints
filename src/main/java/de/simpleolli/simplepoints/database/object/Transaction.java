package de.simpleolli.simplepoints.database.object;

import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Transaction {

    private String transactionID;
    private String senderName;
    private String receiverName;
    private Integer pointCount;
    private String dateString;

    public Transaction(String senderName, String receiverName, Integer pointCount) {
        this.transactionID = UUID.randomUUID().toString().split("-")[0];
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.pointCount = pointCount;
        this.dateString = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public Integer getPointCount() {
        return pointCount;
    }

    public String getDateString() {
        return dateString;
    }
}

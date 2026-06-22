package com.museum.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int museumId;
    private String visitorName;
    private String visitorEmail;
    private String visitorPhone;
    private String visitorType;
    private int quantity;
    private BigDecimal totalPrice;
    private Timestamp purchaseDate;

    public Transaction() {}

    public Transaction(int museumId, String visitorName, String visitorEmail, 
                       String visitorPhone, String visitorType, int quantity, 
                       BigDecimal totalPrice) {
        this.museumId = museumId;
        this.visitorName = visitorName;
        this.visitorEmail = visitorEmail;
        this.visitorPhone = visitorPhone;
        this.visitorType = visitorType;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public int getMuseumId() { return museumId; }
    public void setMuseumId(int museumId) { this.museumId = museumId; }
    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }
    public String getVisitorEmail() { return visitorEmail; }
    public void setVisitorEmail(String visitorEmail) { this.visitorEmail = visitorEmail; }
    public String getVisitorPhone() { return visitorPhone; }
    public void setVisitorPhone(String visitorPhone) { this.visitorPhone = visitorPhone; }
    public String getVisitorType() { return visitorType; }
    public void setVisitorType(String visitorType) { this.visitorType = visitorType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public Timestamp getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Timestamp purchaseDate) { this.purchaseDate = purchaseDate; }
} 
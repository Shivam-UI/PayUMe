package com.lgt.paykredit.Models;

public class ModelPurchaseOrder {

    private String date,name,purchaseID,amount;

    public ModelPurchaseOrder(String date, String name, String purchaseID, String amount) {
        this.date = date;
        this.name = name;
        this.purchaseID = purchaseID;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(String purchaseID) {
        this.purchaseID = purchaseID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

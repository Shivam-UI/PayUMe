package com.lgt.paykredit.Models;

/**
 * Created by Ranjan on 3/20/2020.
 */
public class ModelInvoiceItems {
    private String description,amount,qty;

    public ModelInvoiceItems(String description, String amount, String qty) {
        this.description = description;
        this.amount = amount;
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}

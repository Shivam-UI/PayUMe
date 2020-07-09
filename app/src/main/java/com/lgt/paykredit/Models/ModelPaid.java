package com.lgt.paykredit.Models;

/**
 * Created by Ranjan on 3/20/2020.
 */
public class ModelPaid {
    private String invoiceNumber, date, name, amount,status;

    public ModelPaid(String invoiceNumber, String date, String name, String amount, String status) {
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.name = name;
        this.amount = amount;
        this.status = status;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

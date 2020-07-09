package com.lgt.paykredit.Models;

/**
 * Created by Ranjan on 3/19/2020.
 */
public class ModelMerchant {

    private String id,invoiceNumber,extraDueDate,name,invoiceDate,invoiceDueDate,amount,userImage;

    public ModelMerchant(String id, String invoiceNumber, String extraDueDate, String name, String invoiceDate, String invoiceDueDate, String amount, String userImage) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.extraDueDate = extraDueDate;
        this.name = name;
        this.invoiceDate = invoiceDate;
        this.invoiceDueDate = invoiceDueDate;
        this.amount = amount;
        this.userImage = userImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getExtraDueDate() {
        return extraDueDate;
    }

    public void setExtraDueDate(String extraDueDate) {
        this.extraDueDate = extraDueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(String invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}

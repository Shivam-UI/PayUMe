package com.lgt.paykredit.Models;

/**
 * Created by Ranjan on 3/19/2020.
 */
public class ModelAllInvoices {

    private String paidInvoiceNumber,paymentStatus,image,name,amount,paymentDate,PayAdvance,tbl_invoice_customer_id,customer_name;

    public String getTbl_invoice_customer_id() {
        return tbl_invoice_customer_id;
    }

    public void setTbl_invoice_customer_id(String tbl_invoice_customer_id) {
        this.tbl_invoice_customer_id = tbl_invoice_customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public ModelAllInvoices(String paidInvoiceNumber, String paymentStatus, String image, String name, String amount, String paymentDate) {
        this.paidInvoiceNumber = paidInvoiceNumber;
        this.paymentStatus = paymentStatus;
        this.image = image;
        this.name = name;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public String getPayAdvance() {
        return PayAdvance;
    }

    public void setPayAdvance(String payAdvance) {
        PayAdvance = payAdvance;
    }

    public String getPaidInvoiceNumber() {
        return paidInvoiceNumber;
    }

    public void setPaidInvoiceNumber(String paidInvoiceNumber) {
        this.paidInvoiceNumber = paidInvoiceNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}

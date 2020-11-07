package com.lgt.paykredit.Models;

public class ModelInvoiceDetails {
    String type,tbl_invoice_id,invoice_no,invoice_date,due_date,total_discount,sub_total,total_advance,total_balance,paid,customer_name,customer_mobile,customer_email,invoice_customer_id;

    public String getInvoice_customer_id() {
        return invoice_customer_id;
    }

    public void setInvoice_customer_id(String invoice_customer_id) {
        this.invoice_customer_id = invoice_customer_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ModelInvoiceDetails(String tbl_invoice_id, String invoice_no, String invoice_date, String due_date, String total_discount, String sub_total, String total_advance, String total_balance, String paid, String customer_name, String customer_mobile, String customer_email) {
        this.tbl_invoice_id = tbl_invoice_id;
        this.invoice_no = invoice_no;
        this.invoice_date = invoice_date;
        this.due_date = due_date;
        this.total_discount = total_discount;
        this.sub_total = sub_total;
        this.total_advance = total_advance;
        this.total_balance = total_balance;
        this.paid = paid;
        this.customer_name = customer_name;
        this.customer_mobile = customer_mobile;
        this.customer_email = customer_email;
    }

    public String getTbl_invoice_id() {
        return tbl_invoice_id;
    }

    public void setTbl_invoice_id(String tbl_invoice_id) {
        this.tbl_invoice_id = tbl_invoice_id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(String total_discount) {
        this.total_discount = total_discount;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getTotal_advance() {
        return total_advance;
    }

    public void setTotal_advance(String total_advance) {
        this.total_advance = total_advance;
    }

    public String getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }
}

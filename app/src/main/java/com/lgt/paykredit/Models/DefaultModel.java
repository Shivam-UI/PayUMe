package com.lgt.paykredit.Models;

public class DefaultModel {

    String tbl_invoice_customer_id,customer_name,customer_mobile,defaulter_date;

    public DefaultModel(String tbl_invoice_customer_id, String customer_name, String customer_mobile, String defaulter_date) {
        this.tbl_invoice_customer_id = tbl_invoice_customer_id;
        this.customer_name = customer_name;
        this.customer_mobile = customer_mobile;
        this.defaulter_date = defaulter_date;
    }

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

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getDefaulter_date() {
        return defaulter_date;
    }

    public void setDefaulter_date(String defaulter_date) {
        this.defaulter_date = defaulter_date;
    }
}

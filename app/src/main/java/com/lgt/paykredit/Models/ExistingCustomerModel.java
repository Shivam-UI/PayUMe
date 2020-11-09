package com.lgt.paykredit.Models;

public class ExistingCustomerModel {

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    String tbl_invoice_customer_id,customer_name,customer_mobile,customer_email,company_name,GSTIN,GSTINAddress,CIN,billing_address,state;

    public ExistingCustomerModel(String tbl_invoice_customer_id, String customer_name, String customer_mobile, String customer_email, String company_name, String GSTIN, String GSTINAddress, String CIN, String billing_address) {
        this.tbl_invoice_customer_id = tbl_invoice_customer_id;
        this.customer_name = customer_name;
        this.customer_mobile = customer_mobile;
        this.customer_email = customer_email;
        this.company_name = company_name;
        this.GSTIN = GSTIN;
        this.GSTINAddress = GSTINAddress;
        this.CIN = CIN;
        this.billing_address = billing_address;
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

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getGSTIN() {
        return GSTIN;
    }

    public void setGSTIN(String GSTIN) {
        this.GSTIN = GSTIN;
    }

    public String getGSTINAddress() {
        return GSTINAddress;
    }

    public void setGSTINAddress(String GSTINAddress) {
        this.GSTINAddress = GSTINAddress;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
    }
}

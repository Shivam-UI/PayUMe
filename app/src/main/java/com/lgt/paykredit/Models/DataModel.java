package com.lgt.paykredit.Models;

public class DataModel {

    String taxName,taxPrice;

    public DataModel(String taxName, String taxPrice) {
        this.taxName = taxName;
        this.taxPrice = taxPrice;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(String taxPrice) {
        this.taxPrice = taxPrice;
    }
}

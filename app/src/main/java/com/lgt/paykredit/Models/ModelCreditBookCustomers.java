package com.lgt.paykredit.Models;

/**
 * Created by Ranjan on 3/16/2020.
 */
public class ModelCreditBookCustomers {

    private String id,image,name,amount,advanceOrDue,date,customer_mobile;

    public ModelCreditBookCustomers(String id, String image, String name, String amount, String advanceOrDue, String date, String customer_mobile) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.amount = amount;
        this.advanceOrDue = advanceOrDue;
        this.date = date;
        this.customer_mobile = customer_mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAdvanceOrDue() {
        return advanceOrDue;
    }

    public void setAdvanceOrDue(String advanceOrDue) {
        this.advanceOrDue = advanceOrDue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }
}

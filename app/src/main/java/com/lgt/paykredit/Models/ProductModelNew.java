package com.lgt.paykredit.Models;

public class ProductModelNew {
    String id;
    String Name;
    String HSNcode;
    String Tax;
    String Quantity;
    String Rate;
    String Discount;
    String TaxAmt;
    String FinalDiscont;
    String FinalTax;
    String FinalAmount;
    String FinalPayBalance;

    public String getFinalPayToMe() {
        return FinalPayToMe;
    }

    public void setFinalPayToMe(String finalPayToMe) {
        FinalPayToMe = finalPayToMe;
    }

    String FinalPayToMe;

    public String getFinalPayBalance() {
        return FinalPayBalance;
    }

    public void setFinalPayBalance(String finalPayBalance) {
        FinalPayBalance = finalPayBalance;
    }

    public String getFinalAmount() {
        return FinalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        FinalAmount = finalAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHSNcode() {
        return HSNcode;
    }

    public void setHSNcode(String HSNcode) {
        this.HSNcode = HSNcode;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getTaxAmt() {
        return TaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        TaxAmt = taxAmt;
    }

    public String getFinalDiscont() {
        return FinalDiscont;
    }

    public void setFinalDiscont(String finalDiscont) {
        FinalDiscont = finalDiscont;
    }

    public String getFinalTax() {
        return FinalTax;
    }

    public void setFinalTax(String finalTax) {
        FinalTax = finalTax;
    }
}

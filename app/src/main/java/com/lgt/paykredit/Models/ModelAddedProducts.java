package com.lgt.paykredit.Models;

public class ModelAddedProducts {
    private String tbl_invoice_products_id;
    private String name;
    private String hsnCode;
    private String amount;
    private String discount;
    private String tax;
    private String quantity;

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    private String advance;

    public ModelAddedProducts(String tbl_invoice_products_id, String name, String hsnCode, String amount, String discount, String tax, String quantity) {
        this.tbl_invoice_products_id = tbl_invoice_products_id;
        this.name = name;
        this.hsnCode = hsnCode;
        this.amount = amount;
        this.discount = discount;
        this.tax = tax;
        this.quantity= quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTbl_invoice_products_id() {
        return tbl_invoice_products_id;
    }

    public void setTbl_invoice_products_id(String tbl_invoice_products_id) {
        this.tbl_invoice_products_id = tbl_invoice_products_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}

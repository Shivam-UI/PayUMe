package com.lgt.paykredit.Models;

public class ProductViewModel {

    String tbl_create_invoice_products_id,products_id,pro_qnt,pro_name,pro_discount,pro_price;

    public ProductViewModel(String tbl_create_invoice_products_id, String products_id, String pro_qnt, String pro_name, String pro_discount, String pro_price) {
        this.tbl_create_invoice_products_id = tbl_create_invoice_products_id;
        this.products_id = products_id;
        this.pro_qnt = pro_qnt;
        this.pro_name = pro_name;
        this.pro_discount = pro_discount;
        this.pro_price = pro_price;
    }

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
    }

    public String getProducts_id() {
        return products_id;
    }

    public void setProducts_id(String products_id) {
        this.products_id = products_id;
    }

    public String getPro_qnt() {
        return pro_qnt;
    }

    public void setPro_qnt(String pro_qnt) {
        this.pro_qnt = pro_qnt;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_discount() {
        return pro_discount;
    }

    public void setPro_discount(String pro_discount) {
        this.pro_discount = pro_discount;
    }

    public String getTbl_create_invoice_products_id() {
        return tbl_create_invoice_products_id;
    }

    public void setTbl_create_invoice_products_id(String tbl_create_invoice_products_id) {
        this.tbl_create_invoice_products_id = tbl_create_invoice_products_id;
    }
}

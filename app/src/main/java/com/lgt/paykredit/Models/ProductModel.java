package com.lgt.paykredit.Models;

public class ProductModel {
    String ProductId,ProductName,ProductAmt,ProductDis,ProductQua,ProductTax;

    public ProductModel(String productId, String productName, String productAmt, String productDis, String productQua, String productTax) {
        ProductId = productId;
        ProductName = productName;
        ProductAmt = productAmt;
        ProductDis = productDis;
        ProductQua = productQua;
        ProductTax = productTax;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductAmt() {
        return ProductAmt;
    }

    public void setProductAmt(String productAmt) {
        ProductAmt = productAmt;
    }

    public String getProductDis() {
        return ProductDis;
    }

    public void setProductDis(String productDis) {
        ProductDis = productDis;
    }

    public String getProductQua() {
        return ProductQua;
    }

    public void setProductQua(String productQua) {
        ProductQua = productQua;
    }

    public String getProductTax() {
        return ProductTax;
    }

    public void setProductTax(String productTax) {
        ProductTax = productTax;
    }
}

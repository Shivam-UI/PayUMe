package com.lgt.paykredit.extras;

public interface InvoiceDetailsClick {

    void showPreview(String uid);

    void changeDate(String uid);

    void payPayment(String uid,String invoiceno);

    void setDetauld(String uid);

    void startShareData(String InvoiceID);
}

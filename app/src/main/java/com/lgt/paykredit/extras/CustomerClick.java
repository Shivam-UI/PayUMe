package com.lgt.paykredit.extras;

public interface CustomerClick {

    void deleteCustomer(String u_id,int pos);

    void editCustomer(String u_id,int pos);

    void refreshList();
}

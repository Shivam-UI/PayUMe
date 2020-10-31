package com.lgt.paykredit.Models;

public class ModelAddedCustomers {

    private String id,name,image,mobile,email,address,gstNumber;

    public ModelAddedCustomers(String id, String name, String image, String mobile, String email, String address, String gstNumber) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.gstNumber = gstNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }
}

package com.lgt.paykredit.Models;

public class ModelBusiness {
    private String business_category_id,name,image;

    public ModelBusiness(String business_category_id, String name, String image) {
        this.business_category_id = business_category_id;
        this.name = name;
        this.image = image;
    }

    public String getBusiness_category_id() {
        return business_category_id;
    }

    public void setBusiness_category_id(String business_category_id) {
        this.business_category_id = business_category_id;
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
}

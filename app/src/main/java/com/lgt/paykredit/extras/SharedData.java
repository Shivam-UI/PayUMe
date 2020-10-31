package com.lgt.paykredit.extras;

import android.app.Application;

public class SharedData extends Application {
    private String data;
    public String getData() {return data;}
    public void setData(String data) {this.data = data;}
}
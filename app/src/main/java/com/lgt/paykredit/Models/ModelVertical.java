package com.lgt.paykredit.Models;

import java.util.List;

public class ModelVertical {

    public String date;
    List<ModelSingleUserTransaction> list;

    public ModelVertical(String date, List<ModelSingleUserTransaction> list) {
        this.date = date;
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ModelSingleUserTransaction> getList() {
        return list;
    }

    public void setList(List<ModelSingleUserTransaction> list) {
        this.list = list;
    }
}

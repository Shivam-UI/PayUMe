package com.lgt.paykredit.Models;

/**
 * Created by Ranjan on 3/17/2020.
 */
public class ModelSingleUserTransaction {

    public static final int PAID = 0;
    public static final int RECEIVED = 1;
    public static final int TIME = 2;

    int content_type;
    int timeOfTransaction;

    private String id,amount,date,time,paidOrReceived,msg;

    public ModelSingleUserTransaction(int content_type, String id, String amount, String date, String time, String paidOrReceived, int timeOfTransaction,String msg) {
        this.content_type = content_type;
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.paidOrReceived = paidOrReceived;
        this.timeOfTransaction = timeOfTransaction;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static int getPAID() {
        return PAID;
    }

    public static int getRECEIVED() {
        return RECEIVED;
    }

    public static int getTIME() {
        return TIME;
    }

    public int getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public void setTimeOfTransaction(int timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
    }

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPaidOrReceived() {
        return paidOrReceived;
    }

    public void setPaidOrReceived(String paidOrReceived) {
        this.paidOrReceived = paidOrReceived;
    }
}
